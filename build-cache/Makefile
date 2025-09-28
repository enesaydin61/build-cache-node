# Gradle Build Cache Node Makefile (Podman)
# Variables
IMAGE_NAME = gradle-build-cache-node
CONTAINER_NAME = gradle-cache-node
CACHE_PORT = 5071
CACHE_DIR = ./cache

# Default target
.PHONY: help
help:
	@echo "Available commands:"
	@echo "  build    - Build the Podman image"
	@echo "  run      - Run the build cache node container"
	@echo "  stop     - Stop the build cache node container"
	@echo "  restart  - Restart the build cache node container"
	@echo "  logs     - Show container logs"
	@echo "  status   - Show container status"
	@echo "  clean    - Remove container and image"
	@echo "  shell    - Open shell in running container"

# Build Podman image
.PHONY: build
build:
	podman build -t $(IMAGE_NAME) .

# Run container in detached mode
.PHONY: run
run:
	@mkdir -p $(CACHE_DIR)
	podman run -d \
		--name $(CONTAINER_NAME) \
		-p $(CACHE_PORT):5071 \
		-v $(PWD)/$(CACHE_DIR):/opt/build-cache-node/cache \
		--restart unless-stopped \
		$(IMAGE_NAME)
	@echo "Build cache node is running on http://localhost:$(CACHE_PORT)"

# Stop container
.PHONY: stop
stop:
	podman stop $(CONTAINER_NAME) || true

# Restart container
.PHONY: restart
restart: stop run

# Show logs
.PHONY: logs
logs:
	podman logs -f $(CONTAINER_NAME)

# Show container status
.PHONY: status
status:
	@podman ps -a --filter name=$(CONTAINER_NAME) --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# Clean up
.PHONY: clean
clean:
	podman stop $(CONTAINER_NAME) || true
	podman rm $(CONTAINER_NAME) || true
	podman rmi $(IMAGE_NAME) || true

# Open shell in running container
.PHONY: shell
shell:
	podman exec -it $(CONTAINER_NAME) /bin/bash

# Build and run
.PHONY: up
up: build run

# Show cache statistics (if available)
.PHONY: cache-stats
cache-stats:
	@curl -s http://localhost:$(CACHE_PORT)/cache/stats | jq . 2>/dev/null || echo "Cache stats not available or jq not installed"
