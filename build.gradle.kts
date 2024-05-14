plugins {
  java
  id("org.springframework.boot") version "3.2.5"
  id("io.spring.dependency-management") version "1.1.4"
}

group = "br"

version = "0.1.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_21 }

repositories { mavenCentral() }

val authorizationServerVersion = "1.2.4"
val findBugsVersion = "jsr305:3.0.2"
val gsonVersion = "2.10.1"
val h2Version = "2.2.224"
val lombokMapStructBindingVersion = "0.1.0"
val lombokVersion = "1.18.32"
val mapStructVersion = "1.5.5.Final"
val postgresqlVersion = "42.1.4"
val springBootVersion = "3.2.5"
val springDocVersion = "2.5.0"

dependencies {
  annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")
  annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapStructBindingVersion")
  annotationProcessor("org.projectlombok:lombok:$lombokVersion")
  compileOnly("org.mapstruct:mapstruct:$mapStructVersion")
  compileOnly("org.projectlombok:lombok:$lombokVersion")
  implementation("com.google.code.findbugs:$findBugsVersion")
  implementation("com.google.code.gson:gson:$gsonVersion")
  implementation("com.h2database:h2:$h2Version")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
  implementation(
    "org.springframework.security:spring-security-oauth2-authorization-server:$authorizationServerVersion"
  )
  runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
  testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.jar { manifest { attributes["Main-Class"] = "br.dubidubibank.DubidubibankApplication" } }
