FROM eclipse-temurin:17
LABEL maintainer="mdomingu@mail.com"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} dan-gw.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/dan-gw.jar"]

