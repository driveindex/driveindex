plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.6.0.202305301015-r")
    implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.jsch:6.6.0.202305301015-r") {
        // com.jcraft.jsch.JSchException: invalid privatekey: xxx
        exclude("com.jcraft", "jsch")
    }
    // https://github.com/mwiede/jsch#by-replacing-a-direct-maven-dependency
    implementation("com.github.mwiede:jsch:0.2.10")
}