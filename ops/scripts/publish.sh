#!/usr/bin/env bash
set -euo pipefail

# Build fat jar
echo "Generating jar with runtime dependencies..."
./gradlew shadowJar
echo "Done!"

# Upload fat jar to S3
echo "Copying jar to S3..."
aws s3 cp ./build/libs/petrol-price-notifier.jar s3://petrol-price-notifier/petrol-price-notifier-${GITHUB_SHA:0:8}.jar
echo "Done!"

# Package template.yaml -> packaged-template.yaml
echo "Packaging template.yaml..."
aws cloudformation package \
--template-file ./ops/deployment/template.yaml \
--s3-bucket petrol-price-notifier \
--output-template-file ./ops/deployment/packaged-template.yaml
echo "Done!"
