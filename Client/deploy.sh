#!/bin/bash
set -e
docker image prune -f
docker-compose up -d rabbitMq-rest
sleep 10
docker-compose up -d

