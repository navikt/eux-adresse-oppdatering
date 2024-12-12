FROM gcr.io/distroless/java21
COPY target/eux-adresse-oppdatering.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
