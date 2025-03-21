plugins {
	java
	jacoco
	`maven-publish`
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.javadoc-links") version "8.13"
	id("io.freefair.javadoc-utf-8") version "8.13"
	id("io.freefair.lombok")        version "8.13"
	id("com.github.ben-manes.versions") version "0.52.0"
	id("me.qoomon.git-versioning") version "6.4.4"
}

group = "ru.digilabs.alkir"

gitVersioning.apply {
	refs {
		considerTagsOnBranches = true
		tag("v(?<tagVersion>[0-9].*)") {
			version = "\${ref.tagVersion}\${dirty:-SNAPSHOT}"
		}
		branch(".+") {
			version = "\${ref}-\${commit.short}\${dirty:-SNAPSHOT}"
		}
	}

	rev {
		version = "\${commit.short}\${dirty:-SNAPSHOT}"
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.compilerArgs.add("-Xlint:unchecked")
	options.compilerArgs.add("-Xlint:deprecation")
	options.compilerArgs.add("-parameters")
}

tasks.jar {
	enabled = false
}

configurations {
	compileOnly {
		extendsFrom(annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	maven("https://jitpack.io")
}

extra["springBootAdminVersion"] = "3.4.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.retry:spring-retry")

	implementation("de.codecentric:spring-boot-admin-starter-server")
	implementation("de.codecentric:spring-boot-admin-starter-client")

	implementation("com.github.LimeChain:jsonrpc4j:1.7.0")
	implementation("javax.jws:javax.jws-api:1.1")

	implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", "2.8.5")
	implementation("org.springdoc", "springdoc-openapi-starter-webmvc-api", "2.8.5")
//	implementation("org.springdoc", "springdoc-openapi-security", "1.8.0")

	implementation("org.jboss.netty", "netty", "3.2.10.Final")

	implementation("com._1c.v8", "core", "1.0.2")
	implementation("com._1c.v8", "ibis.admin", "1.6.8")
	implementation("com._1c.v8", "ibis", "1.1.1")
	implementation("com._1c.v8", "ibis.swp", "1.1.1")
	implementation("com._1c.v8", "swp", "1.0.2")
	implementation("com._1c.v8", "swp.netty", "1.0.2")

	implementation("org.awaitility", "awaitility", "4.3.0")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
	}
}

tasks.test {
	useJUnitPlatform()

	testLogging {
		events("passed", "skipped", "failed", "standard_error")
	}

	reports {
		html.required.set(true)
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		xml.outputLocation.set(File("${layout.buildDirectory.get()}/reports/jacoco/test/jacoco.xml"))
	}
}

publishing {
	publications {
		create<MavenPublication>("ibis-core") {
			artifact(file("ibis/lib/com._1c.v8.core-1.0.2.jar")) {
				groupId = "com._1c.v8"
				artifactId = "core"
				version = "1.0.2"
			}
		}
		create<MavenPublication>("ibis-admin") {
			artifact(file("ibis/lib/com._1c.v8.ibis.admin-1.6.8.jar")) {
				groupId = "com._1c.v8"
				artifactId = "ibis.admin"
				version = "1.6.8"
			}
		}
		create<MavenPublication>("ibis-swp") {
			artifact(file("ibis/lib/com._1c.v8.ibis.swp-1.1.1.jar")) {
				groupId = "com._1c.v8"
				artifactId = "ibis.swp"
				version = "1.1.1"
			}
		}
		create<MavenPublication>("ibis") {
			artifact(file("ibis/lib/com._1c.v8.ibis-1.1.1.jar")) {
				groupId = "com._1c.v8"
				artifactId = "ibis"
				version = "1.1.1"
			}
		}
		create<MavenPublication>("swp-netty") {
			artifact(file("ibis/lib/com._1c.v8.swp.netty-1.0.2.jar")) {
				groupId = "com._1c.v8"
				artifactId = "swp.netty"
				version = "1.0.2"
			}
		}
		create<MavenPublication>("swp") {
			artifact(file("ibis/lib/com._1c.v8.swp-1.0.2.jar")) {
				groupId = "com._1c.v8"
				artifactId = "swp"
				version = "1.0.2"
			}
		}
	}
}

tasks.register<GradleBuild>("publishIbisToMavenLocal") {
	group = "publishing"
	tasks = listOf(
		"publishIbis-adminPublicationToMavenLocal",
		"publishIbis-corePublicationToMavenLocal",
		"publishIbis-swpPublicationToMavenLocal",
		"publishIbisPublicationToMavenLocal",
		"publishSwp-nettyPublicationToMavenLocal",
		"publishSwpPublicationToMavenLocal"
	)
}
