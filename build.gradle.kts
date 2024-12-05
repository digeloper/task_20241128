import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("kapt") version "1.7.10"
	kotlin("plugin.spring") version "1.9.25"
	id("io.kotest") version "0.4.10" apply false
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	jacoco
}

group = "com.digeloper"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

val appMainClassName = "com.digeloper.homework.ApplicationKt"
tasks.getByName<BootJar>("bootJar") {
	mainClass.set(appMainClassName)
}

apply(plugin = "kotlin-kapt")

dependencies {
	// Springboot
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	// prometheus
	implementation("io.micrometer:micrometer-registry-prometheus")

	// kafka
	implementation("org.springframework.kafka:spring-kafka")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.7.0")

	// DB
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Mapper
	implementation("org.mapstruct:mapstruct:1.5.2.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.2.Final")
	kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")
	kaptTest("org.mapstruct:mapstruct-processor:1.5.2.Final")

	// Json
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

	// Utils
	implementation("commons-io:commons-io:2.17.0")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testImplementation("io.mockk:mockk:1.13.12")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("io.projectreactor:reactor-test:3.7.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

jacoco {
	toolVersion = "0.8.12"
	reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

tasks.jacocoTestReport {
	reports {
		xml.required = false
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}
