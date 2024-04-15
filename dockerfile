# Stage 1: Build
FROM gradle:8.5.0-jdk8 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

# Stage 2: Setup
FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache bash curl

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/instaBioBot.jar /app/

COPY start.sh /app/

RUN adduser --disabled-password --gecos '' --shell /usr/sbin/nologin user

RUN chmod +x /app/start.sh

RUN chown -R user:user /app

USER user

# Stage 3: Start
CMD ["bash", "/app/start.sh"]