plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.ellebrecht"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven {
        name = "spring-milestone"
        url = uri("https://repo.spring.io/milestone")
    }
    maven {
        name = "spring-snapshot"
        url = uri("https://repo.spring.io/snapshot")
    }
}

dependencies {
    implementation("org.springframework.data:spring-data-neo4j:7.2.5-GH-2884-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
