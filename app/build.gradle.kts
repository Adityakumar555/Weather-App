import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

      /*  val key: String = project.findProperty("KEY")?.toString() ?: "04f5082c29c8cbf0cd2d99fed6b8ca53"
        buildConfigField("String", "API_KEY", "\"$key\"")
*/

        val properties = Properties()
        val apiKey: String

        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            // This loads the properties from the input stream of the file into the Properties object.
            // This gets an InputStream for reading the local.properties file. It allows the Properties class to load the file's content.
            properties.load(localPropertiesFile.inputStream())
            //This retrieves the value of the API_KEY property from the Properties object,
            apiKey = properties.getProperty("API_KEY")
        } else {
            //The code tries to fetch the API_KEY from environment variables
            apiKey = System.getenv("API_KEY") ?: ""
        }

        // This is a Gradle function used to define a field in the BuildConfig class.
        // access build-specific configurations at runtime.
        buildConfigField(
            "String",
            "API_KEY",
            "\"$apiKey\""
        )


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
    buildFeatures{
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

}