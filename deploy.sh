#!/bin/bash
set -e

# it is required that build have been run before this execution.

pushd Client
./deploy.sh
popd