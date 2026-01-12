#!/bin/bash

echo "🚀 Starting deployment..."

# Збірка Maven проєкту
echo "📦 Building Maven project..."
mvn clean package

# Збірка Docker image
echo "🐳 Building Docker image..."
docker build -t chat-app:1.0 .

echo "✅ Deployment preparation completed!"
echo "Now you can run:"
echo "  - Docker Compose: docker-compose up"
echo "  - Kubernetes: kubectl apply -f k8s/"
