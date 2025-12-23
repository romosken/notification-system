#!/bin/bash
# 1. Create the Dead Letter Queue (DLQ)
aws sqs create-queue --queue-name message-queue-dlq

# 2. Get the ARN of the DLQ
DLQ_ARN=$(aws sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/message-queue-dlq \
  --attribute-names QueueArn --output text | awk '{print $2}')

# 3. Create the Main Queue with Redrive Policy (5 retries)
# maxReceiveCount: 5 means it fails 5 times before moving to DLQ
aws sqs create-queue --queue-name message-queue --attributes '{
  "RedrivePolicy": "{\"deadLetterTargetArn\":\"'"$DLQ_ARN"'\",\"maxReceiveCount\":\"5\"}",
  "VisibilityTimeout": "30"
}'

echo "Queues created: message-queue (with 5 retries) and message-queue-dlq"