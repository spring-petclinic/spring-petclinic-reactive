# Stage 1 : Build in a first stage providing required tools
FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD
MAINTAINER Cedrick Lunven
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

# Stage 2: Packaging the deliverable
FROM adoptopenjdk/openjdk11:alpine
EXPOSE 8081
EXPOSE 8082
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/spring-petclinic-reactive-1.0.0-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "spring-petclinic-reactive-1.0.0-SNAPSHOT.jar"]