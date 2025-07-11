plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.equipocuatro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.equipocuatro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Biometric
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha04")

    //NavComponent
    val navVersion = "2.7.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //Fragmentos
    val fragment_version = "1.7.0"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    //User interface components
    val materialVersion= "1.4.0"
    implementation("com.google.android.material:material:$materialVersion")

    //retrofit library
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //room
    val room_version = "2.5.2"
    implementation ("androidx.room:room-ktx:$room_version")
    implementation ("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    //Coroutines
    val coroutinesVersion = "2.8.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    //implementation("androidx.lifecycle:lifecycle-runtime-ktx:$coroutinesVersion")

    // LiveData
    val liveDataVersion = "2.3.1"
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$liveDataVersion")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    //lottiejson
    val lottieVersion = "3.4.0"
    implementation("com.airbnb.android:lottie:$lottieVersion")
}