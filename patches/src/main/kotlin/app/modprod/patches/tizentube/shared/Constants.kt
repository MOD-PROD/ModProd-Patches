package app.modprod.patches.tizentube.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {

    const val TIZENTUBE_PACKAGE = "io.gh.reisxd.tizentube.cobalt"
    const val YOUTUBE_PACKAGE = "com.google.android.youtube"

    /**
     * TizenTube Cobalt: a standalone Cobalt build that runs the YouTube TV web app
     * on Android TV. It is NOT a modified copy of the official YouTube for Android TV.
     *
     * Currently pinned to 2.0.3 (single APK: cobalt-arm.apk / cobalt-arm64.apk).
     */
    val COMPATIBILITY_TIZENTUBE = Compatibility(
        name = "TizenTube",
        packageName = TIZENTUBE_PACKAGE,
        apkFileType = ApkFileType.APK,
        appIconColor = 0xFF0000,
        targets = listOf(
            AppTarget(version = "2.0.3"),
        ),
    )

    /**
     * YouTube (mobile). Used only by version-independent patches such as "Change app name".
     */
    val COMPATIBILITY_YOUTUBE = Compatibility(
        name = "YouTube",
        packageName = YOUTUBE_PACKAGE,
        apkFileType = ApkFileType.APK_REQUIRED,
        appIconColor = 0xFF0033,
        targets = listOf(
            AppTarget(version = null),
        ),
    )
}
