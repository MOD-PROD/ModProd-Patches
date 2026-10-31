extension {
    name = "extensions/extension.mpe"
}

android {
    namespace = "app.modprod.extension"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly(libs.annotation)
}
