#!/bin/bash
set -e
mvn clean package

docker build . -t rest-service