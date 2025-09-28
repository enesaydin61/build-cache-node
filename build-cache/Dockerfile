# Gradle Build Cache Node (Podman Compatible)
FROM gradle/build-cache-node:latest

# Copy configuration file
COPY config.yaml /opt/build-cache-node/config.yaml

# Expose the default port
EXPOSE 5071

# Create cache directory with proper permissions
RUN mkdir -p /opt/build-cache-node/cache && \
    chmod 755 /opt/build-cache-node/cache

# Set working directory
WORKDIR /opt/build-cache-node

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:5071/status || exit 1

# Start the build cache node with proper arguments
CMD ["/opt/build-cache-node/bin/build-cache-node", "start", "-d", "/opt/build-cache-node/cache", "-c", "/opt/build-cache-node"]
