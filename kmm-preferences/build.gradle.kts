plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka")
}

group = "de.gal-digital"
version = Version.preferences

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("debug", "release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Serialization.json)
            }
        }
        // androidMain & iosMain werden durch androidTarget()/applyDefaultHierarchyTemplate()
        // automatisch konfiguriert; iosMain gruppiert iosX64Main, iosArm64Main und iosSimulatorArm64Main.
    }
}
android {
    namespace = "de.galdigital.preferences"
    compileSdk = Version.Android.targetSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Version.Android.minSdk
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/gal-digital-gmbh/kmm-preferences")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
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
