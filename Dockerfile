FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /home/seleniumframework

COPY pom.xml ./pom.xml
COPY src ./src
COPY testNG-Cucumber ./testNG-Cucumber

ENV DISPLAY=:99.0

ENTRYPOINT ["mvn", "clean", "test"]
