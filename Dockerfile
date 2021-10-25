FROM openjdk:11.0.3-jdk-stretch
ADD /target/HolidayInfoApp-1.0-*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom ", "app.jar"]