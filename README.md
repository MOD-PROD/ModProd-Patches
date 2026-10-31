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

#### A list of your patches will automatically be shown here after your first release is created.

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
