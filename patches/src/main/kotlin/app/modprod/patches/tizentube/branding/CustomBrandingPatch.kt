package app.modprod.patches.tizentube.branding

import app.modprod.patches.tizentube.shared.Constants.COMPATIBILITY_TIZENTUBE
import app.modprod.patches.tizentube.shared.Constants.COMPATIBILITY_YOUTUBE
import app.modprod.patches.tizentube.shared.Constants.TIZENTUBE_PACKAGE
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.folderOption
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.util.asSequence
import app.morphe.util.getNode
import org.w3c.dom.Element
import java.io.File
import java.util.logging.Logger

/**
 * Root of the bundled artwork inside the .mpp.
 *
 *   tizentube/branding/icon/<density>.png    -> android:icon
 *   tizentube/branding/banner/<density>.png  -> android:banner
 */
private const val BRANDING_ROOT = "/tizentube/branding"

private val DENSITIES = listOf("mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi")

/** Anchor class so bundled resources are loaded through the patch bundle's own classloader. */
private object BrandingResources

private val logger = Logger.getLogger("CustomBrandingPatch")

/**
 * Maps a resource directory to the density bucket of the artwork to copy.
 */
private fun densityOf(directory: String): String? {
    val qualifiers = directory.split('-').drop(1)
    return when {
        "anydpi" in qualifiers -> null
        "nodpi" in qualifiers -> "xxxhdpi"
        else -> DENSITIES.lastOrNull { it in qualifiers } ?: "mdpi"
    }
}

/**
 * Custom branding for TizenTube Cobalt (and optionally YouTube name only).
 *
 * Combines app rename + icon + Android TV banner into a single patch,
 * inspired by the official Morphe "Custom branding" patch.
 *
 * Options:
 *  - App name line 1 / line 2   (two separate fields; empty line 2 = single line)
 *  - Custom icon                (folder with mdpi.png … xxxhdpi.png)
 *  - Custom banner              (folder with mdpi.png … xxxhdpi.png, 16:9)
 *  - Replace icon / banner      (bool)
 *
 * The app name is written into strings.xml and referenced from the manifest
 * (more reliable than a literal attribute value).
 *
 * If no custom folder is provided, the bundled YouTube-style artwork is used.
 * Resource names are read from the manifest so the patch survives resource renames.
 */
@Suppress("unused")
val customBrandingPatch = resourcePatch(
    name = "Custom branding",
    description = "Changes the app name, icon and Android TV banner. " +
        "Supports custom artwork folders or the bundled YouTube-style assets.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TIZENTUBE, COMPATIBILITY_YOUTUBE)

    val appNameLine1 by stringOption(
        key = "appNameLine1",
        default = "YouTube",
        values = mapOf(
            "YouTube" to "YouTube",
            "YouTube MOD" to "YouTube MOD",
            "TizenTube" to "TizenTube",
            "TizenTube MOD" to "TizenTube MOD",
            "ModProd" to "ModProd",
        ),
        title = "App name — line 1",
        description = "First line of the name shown under the icon / banner. Required if you want to rename.",
        required = false,
    ) {
        it == null || (it.isNotBlank() && it.trim().first() !in "@?")
    }

    val appNameLine2 by stringOption(
        key = "appNameLine2",
        default = "Premium",
        values = mapOf(
            "Premium" to "Premium",
            "MOD" to "MOD",
            "(empty — single line)" to "",
        ),
        title = "App name — line 2",
        description = "Second line (optional). Leave empty for a single-line name. " +
            "Whether two lines are displayed depends on the launcher " +
            "(many Android TV launchers show only one line).",
        required = false,
    ) {
        it == null || it.isBlank() || it.trim().first() !in "@?"
    }

    val customIcon by folderOption(
        key = "customIcon",
        title = "Custom icon",
        description = """
            Folder containing custom launcher icons.
            
            Expected layout:
            - mdpi.png (or icon/mdpi.png)
            - hdpi.png
            - xhdpi.png
            - xxhdpi.png
            - xxxhdpi.png
            
            Recommended sizes: 48 → 192 px (or higher).
            If empty, the bundled YouTube-style icon is used.
        """.trimIndent(),
        required = false,
    )

    val customBanner by folderOption(
        key = "customBanner",
        title = "Custom banner",
        description = """
            Folder containing custom Android TV banners (16:9).
            
            Expected layout:
            - mdpi.png (or banner/mdpi.png)
            - hdpi.png
            - xhdpi.png
            - xxhdpi.png
            - xxxhdpi.png
            
            Reference size: 320×180 (mdpi). Higher densities scale up.
            If empty, the bundled YouTube-style banner is used.
        """.trimIndent(),
        required = false,
    )

    val replaceIcon by booleanOption(
        key = "replaceIcon",
        default = true,
        title = "Replace icon",
        description = "Replace android:icon (app list, settings, phone launchers).",
    )

    val replaceBanner by booleanOption(
        key = "replaceBanner",
        default = true,
        title = "Replace banner",
        description = "Replace android:banner (Android TV / Google TV home screen).",
    )

    execute {
        // ── 1. App name (via strings.xml) ────────────────────────────────────
        val line1 = appNameLine1?.trim().orEmpty()
        val line2 = appNameLine2?.trim().orEmpty()

        if (line1.isNotBlank()) {
            // In strings.xml a real newline is collapsed to a space by aapt2, the \n escape is kept.
            val displayName = if (line2.isNotBlank()) "$line1\\n$line2" else line1
            val stringName = "modprod_app_name"

            // Write / update the string resource
            val stringsPath = "res/values/strings.xml"
            document(stringsPath).use { document ->
                val root = document.getNode("resources") as Element

                // Remove any existing entry with the same name
                root.getElementsByTagName("string").asSequence()
                    .map { it as Element }
                    .filter { it.getAttribute("name") == stringName }
                    .toList()
                    .forEach { it.parentNode.removeChild(it) }

                val stringElement = document.createElement("string")
                stringElement.setAttribute("name", stringName)
                stringElement.textContent = displayName
                root.appendChild(stringElement)
            }

            // Point manifest labels to the new string resource
            document("AndroidManifest.xml").use { document ->
                val application = document.getNode("application") as Element
                val originalLabel = application.getAttribute("android:label")
                if (originalLabel.isEmpty()) {
                    throw PatchException("<application> has no android:label")
                }

                val newLabelRef = "@string/$stringName"
                application.setAttribute("android:label", newLabelRef)
                var renamed = 1

                for (tag in arrayOf("activity", "activity-alias")) {
                    document.getElementsByTagName(tag).asSequence()
                        .map { it as Element }
                        .filter { it.getAttribute("android:label") == originalLabel }
                        .forEach {
                            it.setAttribute("android:label", newLabelRef)
                            renamed++
                        }
                }

                logger.info(
                    "App renamed to \"${displayName.replace("\\n", " | ")}\" " +
                        "($renamed label(s) → @string/$stringName)",
                )
            }
        }

        // ── 2. Icon & Banner (TizenTube only) ────────────────────────────────
        // YouTube compatibility is name-only; skip artwork for pure YouTube APKs.
        val isTizenTube = packageMetadata.packageName == TIZENTUBE_PACKAGE

        if (!isTizenTube) return@execute

        val iconRefs = mutableSetOf<String>()
        val bannerRefs = mutableSetOf<String>()

        document("AndroidManifest.xml").use { document ->
            val application = document.getNode("application") as Element
            val activities = document.getElementsByTagName("activity").asSequence().map { it as Element }

            (sequenceOf(application) + activities).forEach { element ->
                element.getAttribute("android:icon").takeIf { it.startsWith('@') }?.let(iconRefs::add)
                element.getAttribute("android:roundIcon").takeIf { it.startsWith('@') }?.let(iconRefs::add)
                element.getAttribute("android:banner").takeIf { it.startsWith('@') }?.let(bannerRefs::add)
            }
        }

        if (iconRefs.isEmpty() && bannerRefs.isEmpty()) {
            logger.warning("No android:icon or android:banner found — artwork step skipped")
            return@execute
        }

        fun resolveSource(kind: String, density: String, customFolder: String?): java.io.InputStream? {
            // 1. Try custom folder (supports both flat and nested layouts)
            if (!customFolder.isNullOrBlank()) {
                val folder = File(customFolder.trim())
                val candidates = listOf(
                    File(folder, "$density.png"),
                    File(folder, "$kind/$density.png"),
                    File(folder, "icon/$density.png").takeIf { kind == "icon" },
                    File(folder, "banner/$density.png").takeIf { kind == "banner" },
                ).filterNotNull()

                candidates.firstOrNull { it.isFile }?.let { return it.inputStream() }
            }

            // 2. Fallback to bundled resources
            return BrandingResources::class.java
                .getResourceAsStream("$BRANDING_ROOT/$kind/$density.png")
        }

        fun replaceAll(references: Set<String>, kind: String, customFolder: String?): Int {
            var replaced = 0

            for (reference in references) {
                val type = reference.substringAfter('@').substringBefore('/')
                val name = reference.substringAfter('/')

                val paths = listDecodableFiles("res/$type*/$name*").filter { path ->
                    val fileName = path.substringAfterLast('/')
                    fileName == name || fileName.substringBeforeLast('.') == name
                }

                for (path in paths) {
                    val directory = path.split('/')[1]

                    if (path.endsWith(".xml")) {
                        logger.warning(
                            "$path is an XML drawable (adaptive/vector) — skipped. " +
                                "On API 26+ it may take priority over the bitmaps.",
                        )
                        continue
                    }

                    val density = densityOf(directory) ?: continue
                    val source = resolveSource(kind, density, customFolder)
                        ?: throw PatchException(
                            "Missing artwork for $kind/$density " +
                                "(custom folder empty and bundled asset missing)",
                        )

                    source.use { input -> get(path).outputStream().use(input::copyTo) }
                    replaced++
                }
            }
            return replaced
        }

        val icons = if (replaceIcon == true) {
            replaceAll(iconRefs, "icon", customIcon)
        } else 0

        val banners = if (replaceBanner == true) {
            replaceAll(bannerRefs, "banner", customBanner)
        } else 0

        logger.info(
            "Custom branding: replaced $icons icon file(s) and $banners banner file(s)" +
                (if (customIcon != null) " [custom icon]" else " [bundled icon]") +
                (if (customBanner != null) " [custom banner]" else " [bundled banner]"),
        )

        if (replaceBanner == true && banners == 0 && bannerRefs.isNotEmpty()) {
            logger.warning(
                "No banner bitmap was replaced — the Android TV launcher will still show the old banner.",
            )
        }
    }
}
