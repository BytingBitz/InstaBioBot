#!/bin/bash

# Wait for Selenium
for _ in {1..10}; do
  if curl -s "http://selenium:4444/wd/hub/status" | grep -q "ready"; then
    echo "Selenium is ready!"
    sleep 5
    # Run the jar
    exec "java", "-jar", "instaBioBot.jar"
  fi
  echo "Waiting for Selenium..."
  sleep 5
done
echo "Selenium is not ready, exiting..."
exit 1
