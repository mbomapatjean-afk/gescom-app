# Stage 1: Build
FROM gradle:8.12-jdk21 AS build
WORKDIR /app

# Copie des fichiers de configuration Gradle pour mettre en cache les dépendances
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Téléchargement des dépendances
RUN ./gradlew dependencies --no-daemon || true

# Copie du code source et build
COPY src src
RUN ./gradlew bootWar --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.war app.war
EXPOSE 8079
ENTRYPOINT ["java", "-jar", "app.war"]
