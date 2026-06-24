import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
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
        publishLibraryVariants("release")
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

mavenPublishing {
    // Stages und released via Central Portal (https://central.sonatype.com).
    // OSSRH (oss.sonatype.org) wurde am 30.06.2025 abgeschaltet.
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "kmm-preferences", version.toString())

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )

    pom {
        name.set("kmm-preferences")
        description.set("store preferences in kotlin multiplatform")
        url.set("https://github.com/gal-digital-gmbh/kmm-preferences")
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
            connection.set("scm:git:git://github.com/gal-digital-gmbh/kmm-preferences.git")
            developerConnection.set("scm:git:ssh://github.com/gal-digital-gmbh/kmm-preferences.git")
        }
    }
}

// Zusätzliches Repository für GitHub Packages (von Central Portal unabhängig).
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/gal-digital-gmbh/kmm-preferences")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
