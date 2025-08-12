import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("jacoco")
    id("com.diffplug.spotless") version "7.2.1"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.jetbrains.intellij") version "1.17.4"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "dev.openfga.intellijplugin"
version = "0.1.6"
sourceSets["main"].java.srcDirs("src/main/java", "src/generated/java")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4:4.13.2")
    implementation("dev.openfga:openfga-sdk:0.8.3")
    implementation("org.dmfs:oauth2-essentials:0.22.1")
    implementation("org.dmfs:httpurlconnection-executor:1.22.1")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("dev.openfga:openfga-language:v0.2.0-beta.2")

    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.2.1")

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

spotless {
    java {
        target("src/main/java/dev/openfga/intellijplugin/**/*.java")
        palantirJavaFormat()
        removeUnusedImports()
        importOrder()
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
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
        targetOutputDir.set(file("src/generated/java/dev/openfga/intellijplugin/parsing"))
        purgeOldFiles.set(true)
    }

    generateParser {
        sourceFile.set(file("src/main/java/dev/openfga/intellijplugin/parsing/openfga.bnf"))
        targetRootOutputDir.set(file("src/generated/java"))
        pathToParser.set("dev/openfga/intellijplugin/parsing/OpenFGAParser.java")
        pathToPsiRoot.set("dev/openfga/intellijplugin/psi")
        purgeOldFiles.set(true)
        dependsOn(generateLexer) // The lexer must be generated before parser otherwise we get build errors
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
        sinceBuild.set(project.property("plugin.sinceBuild").toString())
        untilBuild.set(project.property("plugin.untilBuild").toString())
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    register("generateIdeVersionsList") {
        group = "verification"
        description = "Generate IDE versions list for plugin verifier"

        val outputFile = layout.buildDirectory.file("verifier-plugin/intellij-platform-plugin-verifier-action-ide-versions-file.txt")
        outputs.file(outputFile)

        val sinceBuildValue = providers.gradleProperty("plugin.sinceBuild")
        val untilBuildValue = providers.gradleProperty("plugin.untilBuild")

        doLast {
            val BASE_YEAR = 2000

            fun buildNumberToVersion(buildNumber: Int): String? {
                val buildStr = buildNumber.toString()
                if (buildStr.length < 3) return null

                val year = BASE_YEAR + buildStr.substring(0, 2).toInt()
                val version = buildStr.last().toString().toInt()

                if (version > 3) return null

                return "$year.$version"
            }

            fun generateOptimizedVersions(since: Int, until: Int): List<String> {
                val sinceYear = BASE_YEAR + since.toString().substring(0, 2).toInt()
                val untilYear = BASE_YEAR + until.toString().substring(0, 2).toInt()

                val versions = mutableSetOf<String>()

                // Always include the exact since version
                buildNumberToVersion(since)?.let { versions.add(it) }

                // For intermediary years, only include the latest version (.3)
                // We do this to reduce the number of versions while still covering all major releases
                // Otherwise, we would run out of space on default GitHub Actions runners
                for (year in (sinceYear + 1) until untilYear) {
                    val yearCode = year - BASE_YEAR
                    val latestBuildNumber = yearCode * 10 + 3 // Always .3 for intermediary years
                    buildNumberToVersion(latestBuildNumber)?.let { versions.add(it) }
                }

                // Always include the exact until version (if different from since)
                if (until != since) {
                    buildNumberToVersion(until)?.let { versions.add(it) }
                }

                return versions.sorted()
            }

            val sinceVersion = sinceBuildValue.get().replace(".*", "").toInt()
            val untilVersion = untilBuildValue.get().replace(".*", "").toInt()

            println("Optimizing versions from build $sinceVersion to $untilVersion")

            val versions = generateOptimizedVersions(sinceVersion, untilVersion)

            val ideVersions = buildList {
                versions.forEach { version ->
                    add("ideaIC:$version")
                    add("ideaIU:$version")
                }
            }.sorted()

            outputFile.get().asFile.parentFile.mkdirs()
            outputFile.get().asFile.writeText(ideVersions.joinToString("\n"))

            println("Generated optimized ${ideVersions.size} IDE versions (reduced from potential ${(untilVersion - sinceVersion + 1) * 2})")
            println("Versions: ${versions.joinToString(", ")}")
            println("Output: ${outputFile.get().asFile}")
        }
    }
}

