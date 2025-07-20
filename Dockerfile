FROM openjdk:17

WORKDIR /app

COPY target/gestao-de-eventos-backend-0.0.1-SNAPSHOT.jar /app/gestao-de-eventos-backend.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "gestao-de-eventos-backend.jar" ]