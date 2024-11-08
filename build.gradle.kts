import org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.4.0" apply false
	id("org.jetbrains.kotlin.android") version "1.9.23" apply false
	id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
	id("com.google.gms.google-services") version "4.4.2" apply false
	id("com.google.dagger.hilt.android") version "2.50" apply false
	kotlin("plugin.serialization") version "1.9.0" apply false
}

tasks.register("clean", Delete::class) {
	delete(rootProject.layout.buildDirectory)
}
tasks.withType<JavaCompile>().configureEach {
	options.isFork = true
}
tasks.withType<Test>().configureEach {
	maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
	if (!project.hasProperty("createReports")) {
		reports.html.required.value(false)
		reports.junitXml.required.value(false)
	}
}

tasks.withType<KaptWithoutKotlincTask>().configureEach {
	kaptProcessJvmArgs.add("-Xmx512m")
}

buildscript {
	dependencies {
		classpath(libs.hilt.android.gradle.plugin)
	}
}