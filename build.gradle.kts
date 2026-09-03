import org.jetbrains.intellij.platform.gradle.tasks.GenerateLexerTask
import org.jetbrains.intellij.platform.gradle.tasks.GenerateParserTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jetbrains.intellij.platform.grammarkit") version "2.18.1"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        val type = providers.gradleProperty("platformType")
        val version = providers.gradleProperty("platformVersion")
        create(type, version)

        plugins(providers.gradleProperty("platformPlugins").map {
            it.split(',').map(String::trim).filter(String::isNotEmpty)
        })
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map {
            it.split(',').map(String::trim).filter(String::isNotEmpty)
        })

        jflex("1.7.0-2")
    }
}

val genRoot: File = project.file("src/gen").also { genRoot ->
    sourceSets {
        main {
            java.srcDirs(genRoot)
        }
    }

    idea {
        module {
            generatedSourceDirs.add(genRoot)
        }
    }
}

intellijPlatform {
    pluginConfiguration {
        id = properties("pluginGroup")
        name = properties("pluginName")
        version = properties("pluginVersion")

        vendor {
            name = properties("pluginVendor")
        }

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
        }
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    val generateDustParserTask = register<GenerateParserTask>("generateDustParser") {
        description = "Generate Dust parser for IntelliJ Platform Grammar Kit"
        sourceFile.set(project.file("src/main/java/com/github/vilinfield/dust/Dust.bnf"))
        targetRootOutputDir.set(genRoot)
        pathToParser.set("com/github/vilinfield/dust/parser/DustParser.java")
        pathToPsiRoot.set("com/github/vilinfield/dust/psi")
        purgeOldFiles.set(true)

        outputs.upToDateWhen { false }
    }

    val generateDustLexerTask = register<GenerateLexerTask>("generateDustLexer") {
        description = "Generate Dust lexer for IntelliJ Platform Grammar Kit"
        sourceFile.set(project.file("src/main/java/com/github/vilinfield/dust/Dust.flex"))
        targetRootOutputDir.set(genRoot)
        pathToClass.set("DustLexer")
        skeleton.set(project.file("src/main/java/com/github/vilinfield/dust/idea-flex.skeleton"))
        purgeOldFiles.set(true)

        outputs.upToDateWhen { false }
        dependsOn(generateDustParserTask)
    }

    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it

            dependsOn(generateDustLexerTask)
        }
    }
}
