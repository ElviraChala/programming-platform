plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.elvira"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	annotationProcessor("org.projectlombok:lombok:1.18.36")
	compileOnly("org.projectlombok:lombok:1.18.36")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.postgresql:postgresql:42.7.5")

//	testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.0-M1")
//	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.13.0-M2")
//	testImplementation("org.mockito:mockito-core:5.17.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
