// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.2.2" apply false
	id("org.jetbrains.kotlin.android") version "1.9.0" apply false
	id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
	id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}
tasks.withType<JavaCompile>().configureEach {
	options.isFork = true
}
tasks.withType<Test>().configureEach {
	maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
	if (!project.hasProperty("createReports")) {
		reports.html.required = false
		reports.junitXml.required = false
	}
}

buildscript{
	dependencies{
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
	}
}