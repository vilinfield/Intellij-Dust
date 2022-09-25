fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.9.0"
    id("org.jetbrains.grammarkit") version "2021.2.2"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

sourceSets {
    named("main") {
        java.srcDir("src/gen")
    }
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

grammarKit {
    jflexRelease.set("1.7.0-2")
    grammarKitRelease.set("2021.1.2")
}

tasks {
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }
    wrapper {
        gradleVersion = properties("gradleVersion")
    }
    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
    }
    withType<org.jetbrains.grammarkit.tasks.GenerateParserTask> {
        source.set("src/main/java/com/github/vilinfield/dust/Dust.bnf")
        targetRoot.set("src/gen")
        pathToParser.set("com/github/vilinfield/dust/parser/DustParser.java")
        pathToPsiRoot.set("com/github/vilinfield/dust/psi")
        purgeOldFiles.set(true)
    }
    withType<org.jetbrains.grammarkit.tasks.GenerateLexerTask> {
        source.set("src/main/java/com/github/vilinfield/dust/Dust.flex")
        targetDir.set("src/gen/com/github/vilinfield/dust/")
        targetClass.set("DustLexer")
        skeleton.set(file("src/main/java/com/github/vilinfield/dust/idea-flex.skeleton"))
        purgeOldFiles.set(true)
    }
}
