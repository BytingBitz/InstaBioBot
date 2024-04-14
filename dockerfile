# Stage 1: Build
FROM gradle:8.5.0-jdk8 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

# Stage 2: Start
FROM eclipse-temurin:21-jdk-alpine

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/instaBioBot.jar /app/

ENTRYPOINT ["java","-jar","/app/instaBioBot.jar"]

# RUN adduser --disabled-password --gecos '' --shell /usr/sbin/nologin user

# RUN chmod +x start.sh

# RUN chown -R user:user /app

# USER user

# Start the application
# CMD ["bash", "./start.sh"]