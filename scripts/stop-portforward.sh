#!/bin/bash
set -e

if [ -f portforward.pid ]; then
  echo "Terminates port-forward (PID $(cat portforward.pid))..."
  kill $(cat portforward.pid)
  rm portforward.pid
  echo "Port-forward terminated."
else
  echo "None port-forward was found."
fi
