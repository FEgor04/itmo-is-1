#!/bin/bash

# Check if PREFIX, PASSWORD, and API_DOMAIN are set
if [ -z "$PREFIX" ] || [ -z "$PASSWORD" ] ; then
  echo "Please set PREFIX and PASSWORD environment variables."
  exit 1
fi

API_DOMAIN="http://localhost:8080"

# Loop through 10 users
for i in {1..10}; do
  USERNAME="${PREFIX}${i}"

  # Send GET request to the login endpoint
  response=$(curl -s -X POST \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\", \"password\":\"$PASSWORD\"}" \
    "$API_DOMAIN/api/auth/login")

  # Extract the JWT token from the JSON response
  ACCESS_TOKEN=$(echo "$response" | jq -r '.accessToken')

  # Check if the token was extracted successfully
  if [ "$ACCESS_TOKEN" == "null" ] || [ -z "$ACCESS_TOKEN" ]; then
    echo "Request for $USERNAME failed. Response: $response"
  else
    echo "$ACCESS_TOKEN"
  fi
done

