#!/bin/bash

# Wait for Selenium
for _ in {1..25}; do
  if curl -s "http://selenium:4444/wd/hub/status" | grep -q "ready"; then
    echo "[+] Selenium server is ready..."
    sleep 5
    # Run the jar
    exec "java" "-jar" "/app/instaBioBot.jar"
  fi
  echo "[+] Waiting for Selenium server..."
  sleep 5
done
echo "[+] Selenium server unavailable, exiting..."
exit 0
