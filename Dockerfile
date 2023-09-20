FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY /target/fitness-back-0.0.1-SNAPSHOT.jar fitness-back.jar
ENTRYPOINT ["java","-jar","/fitness-back.jar"]