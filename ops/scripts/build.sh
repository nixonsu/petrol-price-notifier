#!/usr/bin/env bash
set -euo pipefail

echo "Building application..."
./gradlew build
echo "Done!"
