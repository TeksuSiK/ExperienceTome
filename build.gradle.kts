plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "pl.teksusik"
version = "0.1-SNAPSHOT"

apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "maven-publish")
apply(plugin = "com.github.johnrengelman.shadow")

repositories {
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT")
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.0-beta.2")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.0-beta.2")
}

tasks.processResources {
    expand(
            "experienceTomeVersion" to version,
    )
}

publishing {
    repositories {
        maven {
            name = "teksusik"
            url = uri("https://repo.teksusik.pl/${if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"}")
            credentials {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))

            // Add external repositories to published artifacts
            pom.withXml {
                val repositories = asNode().appendNode("repositories")
                project.repositories.findAll(closureOf<Any> {
                    if (this is MavenArtifactRepository && this.url.toString().startsWith("https")) {
                        val repository = repositories.appendNode("repository")
                        repository.appendNode("id", this.url.toString().replace("https://", "").replace("/", "-").replace(".", "-").trim())
                        repository.appendNode("url", this.url.toString().trim())
                    }
                })
            }
        }
    }
}