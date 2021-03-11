FROM openjdk:15
COPY ./build/libs/LogAnalyser*.jar /app/LogAnalyser.jar
WORKDIR /app
ENV POSTGRES_PASSWORD=""
ENV POSTGRES_USER=""
ENV POSTGRES_DATABASE=""
ENV POSTGRES_HOST=""
CMD ["java", "-jar", "LogAnalyser.jar"]