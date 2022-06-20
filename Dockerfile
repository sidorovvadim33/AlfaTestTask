FROM openjdk:17.0
EXPOSE 8080
ARG JAR_FILE=build/libs/CurrencyAlfaTestTask-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} CurrencyAlfaTestTask-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/CurrencyAlfaTestTask-0.0.1-SNAPSHOT.jar"]

