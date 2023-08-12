FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar accepted-assignment.jar
ENTRYPOINT ["java","-jar","/accepted-assignment.jar"]