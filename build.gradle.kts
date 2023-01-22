plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "pl.teksusik"
version = "0.1-SNAPSHOT"

apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "maven-publish")

repositories {
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
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