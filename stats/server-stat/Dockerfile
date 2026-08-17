FROM amazoncorretto:21
LABEL authors="kseniazilak"
COPY stats/server-stat/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]