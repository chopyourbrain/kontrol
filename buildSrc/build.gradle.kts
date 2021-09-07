plugins {
    `kotlin-dsl`
}
repositories {
    google()
    mavenCentral()
}
sourceSets.getByName("main") {
    java.srcDirs("src/main/kotlin")
}
