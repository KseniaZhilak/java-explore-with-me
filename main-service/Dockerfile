FROM amazoncorretto:21
LABEL authors="kseniazilak"
COPY main-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]