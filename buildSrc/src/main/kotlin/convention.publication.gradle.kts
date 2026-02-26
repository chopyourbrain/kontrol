import org.gradle.api.publish.maven.MavenPublication
import java.util.Base64

plugins {
    `maven-publish`
    signing
}

val sonatypeUsername: String? = System.getenv("SONATYPE_USERNAME")
    ?: project.findProperty("SONATYPE_USERNAME") as? String

val sonatypePassword: String? = System.getenv("SONATYPE_PASSWORD")
    ?: project.findProperty("SONATYPE_PASSWORD") as? String

val signingKey: String? = System.getenv("SIGNING_KEY")
    ?: project.findProperty("SIGNING_KEY") as? String

val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    ?: project.findProperty("SIGNING_PASSWORD") as? String

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}

publishing {

    repositories {

        if (!sonatypeUsername.isNullOrBlank() &&
            !sonatypePassword.isNullOrBlank()
        ) {

            maven {
                name = "centralPortal"

                url = uri(
                    "https://central.sonatype.com/api/v1/publisher/upload"
                )

                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {

        artifact(tasks.named("javadocJar").get())

        pom {

            name.set(project.name)
            description.set("KMP library ${project.name}")
            url.set("https://github.com/chopyourbrain/kontrol")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }

            developers {
                developer {
                    id.set("chopyourbrain")
                    name.set("Mikhail Kuznetsov")
                    email.set("qtd130@gmail.com")
                }
            }

            scm {
                connection.set("scm:git:github.com/chopyourbrain/kontrol.git")
                developerConnection.set(
                    "scm:git:ssh://git@github.com/chopyourbrain/kontrol.git"
                )
                url.set("https://github.com/chopyourbrain/kontrol")
            }
        }
    }
}

signing {

    if (!signingKey.isNullOrBlank()) {

        val decodedKey = try {
            String(Base64.getDecoder().decode(signingKey))
        } catch (e: Exception) {
            signingKey
        }

        useInMemoryPgpKeys(
            decodedKey,
            signingPassword
        )

        sign(publishing.publications)
    }
}
