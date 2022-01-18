#!/bin/bash
set -e

# Build the token service
pushd RestService
./build.sh
popd