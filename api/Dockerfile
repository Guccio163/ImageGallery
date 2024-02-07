FROM gradle:latest

# Setup workdir
WORKDIR /app

# Copy necessary files
COPY build.gradle .
COPY settings.gradle .
COPY gradlew* .

# Copy source code
COPY src src

EXPOSE 8080

CMD ["gradle", "bootRun", "--args='--spring.profiles.active=docker'"]
