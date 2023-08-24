#!/usr/bin/env bash
set -euo pipefail

echo "Deploying..."
aws cloudformation deploy \
--template-file ./ops/deployment/packaged-template.yaml \
--stack-name PetrolPriceNotifierResources \
--parameter-overrides GitSHA=${GITHUB_SHA:0:8} \
--capabilities CAPABILITY_IAM
echo "Done!"
