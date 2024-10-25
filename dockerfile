# Set working directory
WORKDIR /app

# Copy all files to the container
COPY . .

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Install distribution (build the app)
RUN ./gradlew installDist

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["./build/install/limit-life/bin/limit-life"]
