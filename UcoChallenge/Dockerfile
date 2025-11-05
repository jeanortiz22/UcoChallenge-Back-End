FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY . .
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
ENV TZ=America/Bogota \
    JAVA_TOOL_OPTIONS=""

RUN useradd -r -u 1001 appuser
WORKDIR /opt/app

COPY --from=build /app/target/uco-challenge-0.0.1-SNAPSHOT.jar app.jar

USER 1001
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar app.jar"]
