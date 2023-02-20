FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-service.jar
ENTRYPOINT ["java", "-jar", "/stats-service.jar"]
EXPOSE 9090
