# ---- Build (Maven 3.9 + JDK 21) ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
  
  # Copiamos solo poms primero para cachear dependencias
COPY pom.xml ./
COPY UcoChallengeGateway/pom.xml UcoChallengeGateway/pom.xml
  
  # Descarga dependencias (sin compilar aún)
RUN mvn -q -e -B -DskipTests dependency:go-offline
  
  # Copiamos el resto del código
COPY . .
  
  # Empaquetamos SOLO el gateway y sus módulos requeridos
RUN mvn -q -e -B -DskipTests -pl UcoChallengeGateway -am clean package
  
  # ---- Runtime (JRE 21 liviano) ----
FROM eclipse-temurin:21-jre
WORKDIR /app
  # (opcional) zona horaria
ENV TZ=America/Bogota
  
  # Copiamos el JAR resultante
COPY --from=build /workspace/UcoChallengeGateway/target/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8081
  # Healthcheck sencillo
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8081/actuator/health || exit 1

CMD ["java","-jar","/app/app.jar"]
