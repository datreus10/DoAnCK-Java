FROM openjdk:11
COPY target/final-0.0.1-SNAPSHOT.jar social-network.jar
CMD ["java","-jar","social-network.jar"]