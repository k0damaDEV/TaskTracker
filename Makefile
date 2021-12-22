clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	APP_ENV=dev ./gradlew bootRun


install:
	./gradlew installDist

migrations:
	./gradlew diffChangeLog

test:
	./gradlew test
