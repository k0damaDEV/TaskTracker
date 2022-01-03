clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	APP_ENV=dev ./gradlew bootRun

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

migrations:
	./gradlew diffChangeLog

test:
	./gradlew test

report:
	./gradlew jacocoTestReport
