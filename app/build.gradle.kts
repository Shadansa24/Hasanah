plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.app.hasanah"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.hasanah"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    //eventbus
    implementation("org.greenrobot:eventbus:3.2.0")

    //navigation component
    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation("androidx.navigation:navigation-ui:2.3.5")

    //circle image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //sweetalert dialog
    implementation("com.github.f0ris.sweetalert:library:1.6.2")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    //MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.3.1")
    //dagger hilt
    implementation("com.google.dagger:hilt-android:2.44")
    annotationProcessor("com.google.dagger:hilt-compiler:2.44")

    // Lottie
    implementation("com.airbnb.android:lottie:6.0.0")
    //rounded images
    implementation("com.makeramen:roundedimageview:2.3.0")
    //picasso for loading images
    implementation("com.squareup.picasso:picasso:2.8")

    //chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //retrufit
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
}