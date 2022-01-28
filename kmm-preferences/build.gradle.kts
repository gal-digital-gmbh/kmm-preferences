
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka")
}

group = "de.gal-digital"
version = "0.0.1"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android {
        publishLibraryVariants("debug", "release")
    }
    ios()

    sourceSets {
        val commonMain by getting

        val androidMain by getting

        val iosMain by getting
    }
}
android {
    compileSdkVersion(Version.Android.targetSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Version.Android.minSdk)
        targetSdkVersion(Version.Android.targetSdk)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks {
    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn(dokkaHtml)
        from(dokkaHtml.get().outputDirectory)
    }
}
val sonaTypeUrl = (properties["sonatypeUrl"] as String?)!!
publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri(sonaTypeUrl)
            credentials {
                username = properties["sonatypeUsername"] as String?
                password =  properties["sonatypePassword"] as String?
            }
        }
    }
    publications.withType<MavenPublication> {
        if (name == "jvm") {
            artifact(tasks["javadocJar"])
        }
        pom {
            name.set("kmm-preferences")
            description.set("store preferences in kotlin multiplatform")
            url.set("https://github.com/gal-digital-gmbh/kmm-preferneces")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/gal-digital-gmbh/kmm-preferences/blob/main/LICENSE")
                }
            }
            organization {
                name.set("GAL Digital GmbH")
                url.set("https://www.gal-digital.de/")
            }
            developers {
                developer {
                    id.set("GALMKR")
                    name.set("Michael Krause")
                }
            }
            scm {
                url.set("https://github.com/gal-digital-gmbh/kmm-preferences")
            }
        }
    }
}
signing {
    sign(publishing.publications)
}
