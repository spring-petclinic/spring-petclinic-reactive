####################################################################
# Stage 1 : MAVEN_BUILD
#
# Use a docker image with Maven to build the deliverable
# Build everywhere
####################################################################

FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD
MAINTAINER Cedrick Lunven
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

####################################################################
# Stage 2:  LAYERS_BUILD
#
# The deliverable has been built as layers as we asked for it in the
# pom.xml. We will use those layers to buidld and limit number
# of intermediate layers and leveraging on Docker Cache.
####################################################################

FROM adoptopenjdk:11-jre-hotspot as LAYERS_BUILD
WORKDIR application
ARG JAR_FILE=/build/target/*.jar
COPY --from=MAVEN_BUILD ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
 
####################################################################
# Stage 3: BUILD IMAGE
# 
# We will use those layers to build image limiting number
# of intermediate layers and leveraging on Docker Cache.
####################################################################

FROM adoptopenjdk:11-jre-hotspot
WORKDIR application
EXPOSE 9966
EXPOSE 9967
COPY --from=LAYERS_BUILD application/dependencies/ ./
COPY --from=LAYERS_BUILD application/spring-boot-loader ./
COPY --from=LAYERS_BUILD application/snapshot-dependencies/ ./
COPY --from=LAYERS_BUILD application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

