FROM openjdk:23-slim
COPY build/libs/HelloK8s-1.0.0.jar helloK8s.jar
ENTRYPOINT ["java","-jar","/helloK8s.jar"]
