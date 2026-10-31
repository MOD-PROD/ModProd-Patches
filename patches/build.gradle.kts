group = "app.modprod"

patches {
    about {
        name = "ModProd Patches"
        description = "Custom Morphe patches by ModProd."
        source = "git@github.com:MOD-PROD/ModProd-Patches.git"
        author = "ModProd"
        contact = "https://github.com/MOD-PROD/ModProd-Patches/issues"
        website = "https://github.com/MOD-PROD/ModProd-Patches"
        license = "GPLv3"
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the .mpp file.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    implementation(libs.morphe.patches.library)

    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("app.modprod.patches.util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
