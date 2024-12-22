plugins {
    alias(libs.plugins.astracrypt.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nevidimka655.crypto.tink"
}

dependencies {
    api(libs.google.crypto.tink)
    api(libs.kotlin.serialization)
}