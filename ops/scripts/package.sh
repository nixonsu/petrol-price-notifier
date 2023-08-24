#!/usr/bin/env bash
set -euo pipefail

# Build fat jar
./gradlew shadowJar

# Copy fat jar to S3
aws s3 cp ./build/libs/petrol-price-notifier.jar s3://petrol-price-notifier/petrol-price-notifier-${GITHUB_SHA:0:8}.jar

# Package template.yaml -> packaged-template.yaml with paramters populated e.g. jar name
aws cloudformation package \
--template-file ./ops/deployment/template.yaml \
--output-template-file ./ops/deployment/packaged-template.yaml
