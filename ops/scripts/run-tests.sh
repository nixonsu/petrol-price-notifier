#!/usr/bin/env bash
set -euo pipefail

echo "Running tests..."
./gradlew test
echo "Done!"
