#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Application details
APP_NAME="parcel-tracking-tool"
IMAGE_TAG="latest"
CONTAINER_NAME="parcel-tracking-container"

echo -e "${BLUE}🚀 Building Docker image for $APP_NAME...${NC}"

# Build the Docker image
docker build -t $APP_NAME:$IMAGE_TAG .

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Docker image built successfully!${NC}"
    
    # Check if container is already running
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo -e "${YELLOW}⚠️  Container $CONTAINER_NAME is already running. Stopping it...${NC}"
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
    fi
    
    echo -e "${BLUE}🏃 Running the container...${NC}"
    
    # Run the container
    docker run -d \
        --name $CONTAINER_NAME \
        -p 8080:8080 \
        --restart unless-stopped \
        $APP_NAME:$IMAGE_TAG
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Container started successfully!${NC}"
        echo -e "${BLUE}📱 Application is running at: http://localhost:8080${NC}"
        echo -e "${BLUE}📚 Swagger UI: http://localhost:8080/swagger-ui.html${NC}"
        echo -e "${BLUE}🩺 Health Check: http://localhost:8080/actuator/health${NC}"
        echo -e "${BLUE}💽 H2 Console: http://localhost:8080/h2-console${NC}"
        echo ""
        echo -e "${YELLOW}📋 Useful commands:${NC}"
        echo -e "  - View logs: ${GREEN}docker logs -f $CONTAINER_NAME${NC}"
        echo -e "  - Stop container: ${GREEN}docker stop $CONTAINER_NAME${NC}"
        echo -e "  - Remove container: ${GREEN}docker rm $CONTAINER_NAME${NC}"
        echo -e "  - View running containers: ${GREEN}docker ps${NC}"
    else
        echo -e "${RED}❌ Failed to start the container${NC}"
        exit 1
    fi
else
    echo -e "${RED}❌ Failed to build Docker image${NC}"
    exit 1
fi 