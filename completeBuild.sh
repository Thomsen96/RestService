#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd Messaging-utilities
./build.sh
popd

# Build the token service
pushd RestService
./build.sh
popd