FROM openjdk:11-jre-slim
COPY /target/inside-test-0.0.1-SNAPSHOT.jar /app/inside-test-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/inside-test-0.0.1-SNAPSHOT.jar"]