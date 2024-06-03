plugins {
    id("java")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "dev.openfga.intellijplugin"
version = "0.1.1"
sourceSets["main"].java.srcDirs("src/main/java", "src/generated/java")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4:4.13.1")
    implementation("dev.openfga:openfga-sdk:0.4.2")
    implementation("org.dmfs:oauth2-essentials:0.22.1")
    implementation("org.dmfs:httpurlconnection-executor:1.22.1")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    // Until, https://github.com/openfga/language/pkg/java is published,
    // the plugin cannot be built without manually building language and providing the jar file
    implementation(files("libs/language-0.0.1.jar"))

    testImplementation("junit:junit:4.13.2")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(
        "org.intellij.intelliLang",
        "org.jetbrains.plugins.yaml",
        "com.intellij.java"))
}

grammarKit {
    jflexRelease.set("1.7.0-1")
    grammarKitRelease.set("2021.1.2")
    intellijRelease.set("203.7717.81")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"
    dependsOn("generateLexer", "generateParser")
}

tasks {

    check {
        dependsOn(jacocoTestReport)
    }

    generateLexer {
        sourceFile.set(file("src/main/java/dev/openfga/intellijplugin/parsing/OpenFGALexer.flex"))
        targetDir.set("src/generated/java/dev/openfga/intellijplugin/parsing")
        targetClass.set("OpenFGALexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        sourceFile.set(file("src/main/java/dev/openfga/intellijplugin/parsing/openfga.bnf"))
        targetRoot.set("src/generated/java")
        pathToParser.set("dev/openfga/intellijplugin/parsing/OpenFGAParser.java")
        pathToPsiRoot.set("dev/openfga/intellijplugin/psi")
        purgeOldFiles.set(true)
    }

    test {
        useJUnit()

        configure<JacocoTaskExtension> {
            isEnabled = true
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }

        testLogging {
            showStandardStreams = true
            events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
        }
    }

    jacocoTestReport {
        classDirectories.setFrom(instrumentCode)

        reports {
            xml.required = true
            html.required = true
        }
    }

    jacocoTestCoverageVerification {
        classDirectories.setFrom(instrumentCode)
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("JETBRAINS_API_TOKEN"))
    }
}
