<div align="center">

# ModProd Patches

**Custom Morphe patches by ModProd.**

[![License](https://img.shields.io/badge/license-GPLv3-blue?style=for-the-badge)](LICENSE)

</div>

---

## ❓ About

Collection of custom patches for [Morphe](https://morphe.software).

### How to use these patches

Add this repository as a patch source in Morphe Manager:

**https://morphe.software/add-source?github=MOD-PROD/ModProd-Patches**

Or click the button below from a device with Morphe installed:

<a href="https://morphe.software/add-source?github=MOD-PROD/ModProd-Patches">
  <img src="https://raw.githubusercontent.com/MorpheApp/morphe-patches-template/main/.github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48">
</a>

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0](https://github.com/MOD-PROD/ModProd-Patches/releases/tag/v1.0.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;7 patches total
<details open>
<summary>📦 TizenTube&nbsp;&nbsp;•&nbsp;&nbsp;5 patches</summary>
<br>

**🎯 Supported versions:**

| 2.0.3 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Change app name](#change-app-name) | Changes the app name shown on the launcher and in the app list. | • App name |
| [Clone app](#clone-app) | Changes the package name so the patched app can be installed next to the original TizenTube. Default package name: mod.android.youtube.tv. | • Package name<br>• Update permissions<br>• Update providers |
| [Custom branding](#custom-branding) | Changes the app name, icon and Android TV banner. Supports custom artwork folders or the bundled YouTube-style assets. | • App name — line 1<br>• App name — line 2<br>• Custom icon<br>• Custom banner<br>• Replace icon<br>• Replace banner |
| [Inject Adblocker Userscript](#inject-adblocker-userscript) | Injects a JavaScript userscript into the YouTube TV web app running in Cobalt. The script removes ad placements from InnerTube responses before the app reads them. | • Custom userscript file |
| [MOD Icon and Banner](#mod-icon-and-banner) | Replaces the app icon and the Android TV launcher banner with YouTube artwork. On Android TV the launcher shows the banner, so both are replaced. | • Replace icon<br>• Replace banner |

</details>

<details open>
<summary>📦 YouTube&nbsp;&nbsp;•&nbsp;&nbsp;2 patches</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Change app name](#change-app-name) | Changes the app name shown on the launcher and in the app list. | • App name |
| [Custom branding](#custom-branding) | Changes the app name, icon and Android TV banner. Supports custom artwork folders or the bundled YouTube-style assets. | • App name — line 1<br>• App name — line 2<br>• Custom icon<br>• Custom banner<br>• Replace icon<br>• Replace banner |

</details>

<!-- PATCHES_END -->

## 🏗️ Build

```bash
# Local builds need a GitHub token with read:packages (Morphe dependencies).
# Put it in ~/.gradle/gradle.properties (not in the repository):
#   gpr.user=YOUR_GITHUB_USERNAME
#   gpr.key=ghp_xxx

./gradlew :patches:buildAndroid
# → patches/build/libs/patches-<version>.mpp
```

## 🚀 Releases

Releases are created automatically by [semantic-release](https://semantic-release.gitbook.io) when a
commit is pushed to `main` (stable) or `dev` (pre-release). Use conventional commit messages:

| Prefix | Effect |
|--------|--------|
| `feat:` | minor release |
| `fix:`, `perf:`, `bump:` | patch release |
| `chore:`, `docs:`, `build:` | no release |

Repository settings required: **Settings → Actions → General → Workflow permissions → Read and write permissions**.

## ⚖️ License

GPLv3 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).

Not affiliated with YouTube, Google, TizenTube or the Morphe project.
Use only with apps you are allowed to modify.
