# Stage 1: Build the application
FROM gradle:4.7.0-jdk8-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY src /app/src

RUN ./gradlew build

# Stage 2: Create final image
FROM adoptopenjdk:21-jdk-alpine3.15

WORKDIR /app

COPY --from=builder /app/build/libs/instaBioBot.jar /app/instaBioBot.jar

RUN adduser --disabled-password --gecos '' --shell /usr/sbin/nologin user

RUN chmod +x start.sh

RUN chown -R user:user /app

USER user

# Start the application
CMD ["bash", "./start.sh"]