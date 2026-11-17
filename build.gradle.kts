import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("jacoco")
    id("com.diffplug.spotless") version "8.11.0"
    id("org.jetbrains.kotlin.jvm") version "2.4.21"
    id("org.jetbrains.intellij.platform") version "2.20.1"
    id("org.jetbrains.grammarkit") version "2023.3.0.4"
}

group = "dev.openfga.intellijplugin"
version = "0.1.13" // x-release-please-version
sourceSets["main"].java.srcDirs("src/main/java", "src/generated/java")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("org.antlr:antlr4:4.13.2")
    implementation("dev.openfga:openfga-sdk:0.11.2")
    implementation("org.dmfs:oauth2-essentials:0.22.1")
    implementation("org.dmfs:httpurlconnection-executor:1.22.1")
    implementation("org.apache.commons:commons-lang3:3.21.0")
    implementation("dev.openfga:openfga-language:0.3.0")

    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.11.0")

    testImplementation("junit:junit:4.13.2")

    // IntelliJ Platform dependencies
    intellijPlatform {
        intellijIdeaCommunity("2023.3")
        bundledPlugins(
            "org.intellij.intelliLang",
            "org.jetbrains.plugins.yaml",
            "com.intellij.java"
        )
        pluginVerifier()
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

// Build the plugin's <change-notes> from the current release's section in
// CHANGELOG.md. Section selection matches the shared release workflow's
// parse-release.sh (openfga/.github) so the GitHub release and the Marketplace
// "What's New" render the same content. Falls back to a safe pointer if the
// section is missing rather than failing the publish.
fun changeNotesFromChangelog(rawVersion: String): String {
    val changelog = file("CHANGELOG.md")
    if (!changelog.exists()) return "See the CHANGELOG for release notes."

    // Match the version delimited by non-version characters, as parse-release.sh does.
    val escaped = Regex.escape(rawVersion)
    val versionPattern = Regex("""(^|[^0-9A-Za-z.-])$escaped([^0-9A-Za-z.-]|$)""")

    val section = mutableListOf<String>()
    var found = false
    for (line in changelog.readLines()) {
        if (line.startsWith("## ")) {
            if (found) break
            if (versionPattern.containsMatchIn(line)) {
                found = true
                continue
            }
        }
        if (found) section.add(line)
    }

    if (!found) return "See the CHANGELOG for release notes."

    // Render inline markdown to HTML: `code` -> <code>, [text](url) -> <a>.
    // Escape HTML-special chars first; markdown delimiters survive so the
    // regexes still match. href values also escape " to avoid closing early.
    fun renderInline(text: String): String {
        var s = text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        s = Regex("""\[([^\]]+)]\(([^)]+)\)""").replace(s) { m ->
            val href = m.groupValues[2].replace("\"", "&quot;")
            """<a href="$href">${m.groupValues[1]}</a>"""
        }
        s = Regex("""`([^`]+)`""").replace(s) { m -> "<code>${m.groupValues[1]}</code>" }
        return s
    }

    // ### Group -> <h2>, `* `/`- ` items -> <li>, everything else -> <p>.
    val html = StringBuilder()
    var listOpen = false
    fun closeList() {
        if (listOpen) {
            html.append("</ul>\n")
            listOpen = false
        }
    }
    for (raw in section) {
        val line = raw.trim()
        when {
            line.isEmpty() -> {}
            line.startsWith("### ") -> {
                closeList()
                html.append("<h2>").append(line.removePrefix("### ").trim()).append("</h2>\n")
            }
            line.startsWith("* ") || line.startsWith("- ") -> {
                if (!listOpen) {
                    html.append("<ul>\n")
                    listOpen = true
                }
                val item = line.removePrefix("* ").removePrefix("- ").trim()
                html.append("<li>").append(renderInline(item)).append("</li>\n")
            }
            else -> {
                closeList()
                html.append("<p>").append(renderInline(line)).append("</p>\n")
            }
        }
    }
    closeList()

    val rendered = html.toString().trim()
    return rendered.ifEmpty { "See the CHANGELOG for release notes." }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {
    pluginConfiguration {
        changeNotes.set(provider { changeNotesFromChangelog(project.version.toString()) })
        ideaVersion {
            sinceBuild.set(providers.gradleProperty("plugin.sinceBuild"))
            untilBuild.set(providers.gradleProperty("plugin.untilBuild"))
        }
    }

    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    // IDEs to verify against, as comma-separated "<type code>-<version>" entries
    // (e.g. "IC-2023.3,IU-2026.3"). Defaults to `pluginVerifier.ides` in
    // gradle.properties; CI overrides it with -PpluginVerifier.ides=<one entry>
    // per matrix job so each runner only downloads a single IDE.
    pluginVerification {
        failureLevel.set(listOf(FailureLevel.COMPATIBILITY_PROBLEMS, FailureLevel.INVALID_PLUGIN))
        ides {
            providers.gradleProperty("pluginVerifier.ides").get()
                .split(",")
                .map(String::trim)
                .filter(String::isNotEmpty)
                .forEach { notation ->
                    val (code, version) = notation.split("-", limit = 2)
                    create(IntelliJPlatformType.fromCode(code), version)
                }
        }
    }
}

grammarKit {
    jflexRelease.set("1.7.0-1")
    grammarKitRelease.set("2021.1.2")
    intellijRelease.set("203.7717.81")
}

spotless {
    java {
        target("src/main/java/dev/openfga/intellijplugin/**/*.java")
        palantirJavaFormat("2.102.0")
        removeUnusedImports()
        importOrder()
    }
}

val javaVersion: String by project

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.fromTarget(javaVersion)
    dependsOn("generateLexer", "generateParser")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
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
}
