plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlinx.serialization).apply(false)
}