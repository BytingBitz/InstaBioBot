FROM eclipse-temurin:21-jre-alpine

RUN adduser --disabled-password --gecos '' --shell /usr/sbin/nologin user

WORKDIR /instabiobot

COPY . .

CMD ["./gradlew", "clean", "bootJar"]

COPY /build/libs/*.jar app.jar

# RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN chmod +x start.sh

RUN chown -R user:user /app

USER user

CMD ["bash", "./start.sh"]