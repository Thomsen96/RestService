#!/bin/bash
set -e

# Build all docker images and mvn 
./build.sh

# Startup all services for this service
./deploy.sh 
sleep 5

# Test the services
./test.sh

# Cleanup the build images
docker image prune -f

