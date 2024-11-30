#!/bin/bash

# Check if PREFIX and PASSWORD are set
if [ -z "$PREFIX" ] || [ -z "$PASSWORD" ]; then
  echo "Please set PREFIX and PASSWORD environment variables."
  exit 1
fi

# API domain
API_DOMAIN="http://localhost:8080"

for i in {1..10}; do
  USERNAME="${PREFIX}${i}"
  
  response=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\", \"password\":\"$PASSWORD\"}" \
    "$API_DOMAIN/api/auth/register")

  echo "Request $i: Username=$USERNAME, Response Code=$response"
done

