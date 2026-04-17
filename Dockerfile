FROM bellsoft/liberica-openjdk-debian:21
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]

RUN apt-get update && apt-get install -y \
    fonts-dejavu \
    fonts-liberation \
    fontconfig \
    && fc-cache -fv