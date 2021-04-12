import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.*
import tanvd.kosogor.proxy.publishJar
import java.net.URL

plugins {
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.dokka") version Versions.dokka
    id("tanvd.kosogor") version Versions.kosogor
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}


repositories {
    jcenter()
    mavenCentral()
    maven(url = Repositories.Kotlin.kotlinx)
}

dependencies {
    api("io.ktor:ktor-client:1.4.1")
    testImplementation("io.ktor:ktor-client-mock-jvm:1.4.1")

    api("org.jsoup:jsoup:1.13.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}

tasks.withType<Test>().all {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<DokkaTask>().all {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"

    configuration {
        externalDocumentationLink {
            url = URL("https://jsoup.org/apidocs/")
            packageListUrl = URL("https://jsoup.org/apidocs/element-list")
        }

        externalDocumentationLink {
            url = URL("https://api.ktor.io/${Libraries.ktor.version}/")
        }
    }
}


publishJar {

    publication {
        artifactId = "ktor-jsoup"
    }

    bintray {
        username = System.getenv("BINTRAY_USER")
        secretKey = System.getenv("BINTRAY_KEY")

        repository = "ktor"

        info {
            githubRepo = "https://github.com/T-Fowl/ktor-jsoup.git"
            vcsUrl = "https://github.com/T-Fowl/ktor-jsoup.git"
            license = "MIT"
        }
    }
}