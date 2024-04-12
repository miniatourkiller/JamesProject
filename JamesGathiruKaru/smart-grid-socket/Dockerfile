FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/SmartGrid-0.0.1-SNAPSHOT.jar smartgrid.jar
ENTRYPOINT [ "java", "-jar", "smartgrid.jar" ]
EXPOSE 9092