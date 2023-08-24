#!/usr/bin/env bash
set -euo pipefail

# Build fat jar
./gradlew shadowJar

# Upload fat jar to S3
aws s3 cp ./build/libs/petrol-price-notifier.jar s3://petrol-price-notifier/petrol-price-notifier-${GITHUB_SHA:0:8}.jar

pwd

# Package template.yaml -> packaged-template.yaml
aws cloudformation package \
--template-file ./ops/deployment/template.yaml \
--s3-bucket petrol-price-notifier \
--output-template-file ./ops/deployment/packaged-template.yaml
