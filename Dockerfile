FROM eclipse-temurin:21-jdk
COPY target/AccountOpeningSystem-0.0.1.jar AccountOpeningSystem-0.0.1.jar
ENTRYPOINT ["java","-jar","/AccountOpeningSystem-0.0.1.jar"]
