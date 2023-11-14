plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.petshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.petshop"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("org.jetbrains:annotations:15.0")
    implementation("androidx.appcompat:appcompat:1.4.0-alpha01")
    implementation("androidx.appcompat:appcompat-resources:1.4.0-alpha01")

    //noinspection GradleCompatible,GradleCompatible
    implementation("com.android.support:preference-v7:28.0.0")

    //Google
//    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation(platform("com.google.firebase:firebase-bom:27.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore:23.0.0")
    implementation ("com.google.firebase:firebase-messaging:21.1.0")
    implementation ("com.google.firebase:firebase-database:20.0.0")
    implementation ("com.google.firebase:firebase-storage:20.0.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.1")

    //Bottom Navigation
    implementation  ("com.etebarian:meow-bottom-navigation:1.3.1")

    //Image slider
    implementation  ("com.github.smarteist:autoimageslider:1.4.0")
    implementation  ("com.github.smarteist:autoimageslider:1.4.0")

    //Glide
    implementation  ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor  ("com.github.bumptech.glide:compiler:4.12.0")

    //Picasso
    implementation  ("com.squareup.picasso:picasso:2.71828")

    //volley
    implementation("com.android.volley:volley:1.2.0")

    //Circle image view
    implementation  ("de.hdodenhof:circleimageview:3.1.0")

    //Location
    implementation  ("com.google.android.gms:play-services-maps:17.0.1")
    implementation  ("com.google.android.gms:play-services-places:17.0.0")
    implementation  ("com.google.android.gms:play-services-location:18.0.0")
    implementation  ("com.google.android.gms:play-services-nearby:17.0.0")
    implementation  ("com.google.android.libraries.places:places:2.4.0")
    //noinspection GradleDynamicVersion
    implementation  ("com.google.maps.android:android-maps-utils:0.5+")

    implementation  ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation  ("com.squareup.retrofit2:retrofit:2.6.0")
    implementation  ("com.squareup.retrofit2:converter-gson:2.6.0")

    implementation  ("com.airbnb.android:lottie:3.4.1")

    implementation  ("it.xabaras.android:recyclerview-swipedecorator:1.2.3")

}