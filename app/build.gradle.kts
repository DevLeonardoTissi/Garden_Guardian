plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "br.com.leonardo.gardenguardian"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.leonardo.gardenguardian"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val glanceVersion = "1.0.0"
    implementation("androidx.glance:glance-appwidget:$glanceVersion")
    implementation("androidx.glance:glance-material3:$glanceVersion")

    val vicoVersion = "1.12.0"
    implementation("com.patrykandpatrick.vico:compose-m3:$vicoVersion")

    //Coil
    val coilVersion = "2.4.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")

    //Room Database
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    val accompanistPermissionsVersion = "0.33.2-alpha"
    implementation("com.google.accompanist:accompanist-permissions:$accompanistPermissionsVersion")

    val koinVersion = "3.5.0"
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")

    val navVersion = "2.7.4"
    implementation("androidx.navigation:navigation-compose:$navVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    val lottieVersion = "6.1.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    val googleFontsVersion = "1.5.4"
    implementation("androidx.compose.ui:ui-text-google-fonts:$googleFontsVersion")

    val browserVersion = "1.6.0"
    implementation("androidx.browser:browser:$browserVersion")

    val firebaseDomVersion = "32.5.0"
    implementation(platform("com.google.firebase:firebase-bom:$firebaseDomVersion"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}