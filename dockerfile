# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Install distribution (build the app)
RUN ./gradlew installDist

# Define the port your application will run on
EXPOSE 8080

# Run the app
CMD ["./build/install/limit-life/bin/limit-life"]
