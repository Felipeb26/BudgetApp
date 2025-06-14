plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	id("com.google.gms.google-services")
}

android {
	namespace = "com.batsworks.budget"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.batsworks.budget"
		minSdk = 29
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

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
		kotlinCompilerExtensionVersion = "1.5.0"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
	implementation("androidx.activity:activity-compose:1.9.0")
	implementation("androidx.activity:activity-ktx:1.9.0")
	implementation(platform("androidx.compose:compose-bom:2024.05.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.ui:ui-tooling")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.material3:material3-android:1.2.1")
	implementation("com.google.firebase:firebase-firestore:25.0.0")
	implementation("com.google.firebase:firebase-storage:21.0.0")
	implementation("androidx.work:work-runtime-ktx:2.9.0")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	implementation("androidx.compose.material3:material3:1.2.1")
	implementation("androidx.navigation:navigation-compose:2.7.7")
	//AsyncImage
	implementation("io.coil-kt:coil-compose:2.6.0")
	//Dagger Hilt
	implementation("com.google.dagger:hilt-android:2.50")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	ksp("com.google.dagger:hilt-compiler:2.50")
	ksp("androidx.hilt:hilt-compiler:1.2.0")
	//Lottie
	implementation("com.airbnb.android:lottie-compose:6.0.0")
	//Firebase
	implementation("com.google.firebase:firebase-firestore:25.0.0")
	//Room
	implementation("androidx.room:room-runtime:2.6.1")
	annotationProcessor("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	//Biometric
	implementation("androidx.biometric:biometric:1.1.0")
	//SplashScreen
	implementation("androidx.core:core-splashscreen:1.0.1")
	//Calendar - Date Pick
	implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
	//Rollbar
	implementation("com.rollbar:rollbar-android:1.10.0")
	//YCharts
	implementation("co.yml:ycharts:2.1.0")
}