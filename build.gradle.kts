
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.16.1"
    id("org.jetbrains.grammarkit") version "2022.3.2"

    id("jacoco")
}

group = "com.github.le_yams"
version = "0.2.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.openfga:openfga-sdk:0.4.1")
    implementation("org.dmfs:oauth2-essentials:0.22.0")
    implementation("org.dmfs:httpurlconnection-executor:1.21.3")

    testImplementation("junit:junit:4.13.2")
}


sourceSets["main"].java.srcDirs("src/generated/java", "src/main/java")


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("org.intellij.intelliLang", "org.jetbrains.plugins.yaml"))
}

grammarKit {
    jflexRelease.set("1.7.0-1")
    grammarKitRelease.set("2021.1.2")
    intellijRelease.set("203.7717.81")
}

tasks {

    compileJava {

        dependsOn(
            generateLexer,
            generateParser,
        )
    }

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    generateLexer {
        sourceFile.set(file("src/main/java/com/github/le_yams/openfga4intellij/parsing/OpenFGALexer.flex"))
        targetDir.set("src/generated/java/com/github/le_yams/openfga4intellij/parsing")
        targetClass.set("OpenFGALexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        sourceFile.set(file("src/main/java/com/github/le_yams/openfga4intellij/parsing/openfga.bnf"))
        targetRoot.set("src/generated/java")
        pathToParser.set("com/github/le_yams/openfga4intellij/parsing/RustParser.java")
        pathToPsiRoot.set("com/github/le_yams/openfga4intellij/psi")
        purgeOldFiles.set(true)
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    test {
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)
    }
}
