#!/usr/bin/env bash
set -euo pipefail

# Temporarily disable exit on error
set +e

# Check if bucket to store project jar exists
aws s3api head-bucket --bucket petrol-price-notifier 2>/dev/null

# Capture exit code of the last command
exit_code=$?

# Re-enable exit on error
set -e

# If exit code is 0, the bucket exists; otherwise, it does not, or inadequate permission to access it
if [ $exit_code -eq 0 ]; then
  echo "Bucket exists, skipping creating bucket"
else
  echo "Bucket does not exist, creating bucket"

  # Create S3 bucket to store project jar
  aws s3api create-bucket \
  --bucket petrol-price-notifier \
  --create-bucket-configuration LocationConstraint=${AWS_DEFAULT_REGION}
fi
