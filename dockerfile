# Use an official openjdk runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project to the working directory
COPY . .

# Build the application
RUN ./gradlew installDist

# Expose the port that Ktor will run on (Render will bind the port automatically)
EXPOSE 8080

# Define the command to run the application
CMD ["./build/install/limit-life/bin/your-app-name"]
