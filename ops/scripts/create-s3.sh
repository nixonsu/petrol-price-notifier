#!/usr/bin/env bash
set -euo pipefail

# Check if bucket to store project jar exists
aws s3api head-bucket --bucket petrol-price-notifier 2>/dev/null

# Exit code of last command is stored in $? variable
# If it's 0, the bucket exists; otherwise, it does not, or inadequate permission to access it
if [ $? -eq 0 ]; then
  echo "Bucket exists, skipping creating bucket"
else
  echo "Bucket does not exist, creating bucket"

  # Create S3 bucket to store project jar
  aws s3api create-bucket \
  --bucket petrol-price-notifier \
  --create-bucket-configuration LocationConstraint=ap-southeast-2
fi
