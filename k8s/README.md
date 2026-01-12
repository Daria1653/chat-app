# Chat Application

Крос-платформенний чат-додаток з підтримкою приватних повідомлень, WebSocket та Kubernetes.

## Технології
- Java 17
- Jakarta EE (Servlets, WebSocket)
- PostgreSQL 15
- Docker & Docker Compose
- Kubernetes (k8s)

## Структура проєкту
```
chat-app/
├── src/main/
│   ├── java/com/chat/
│   │   ├── controller/     # REST Controllers
│   │   ├── model/          # Data models
│   │   ├── service/        # Business logic
│   │   └── websocket/      # WebSocket endpoint
│   └── webapp/
│       └── index.html      # Frontend
├── k8s/                    # Kubernetes configs
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Запуск через Docker Compose

1. Збірка проєкту:
```bash
mvn clean package
```

2. Запуск:
```bash
docker-compose up --build
```

3. Відкрити: http://localhost:8080/chat

## Запуск через Kubernetes

1. Встановити k3s/minikube
2. Збудувати Docker image
3. Застосувати конфігурації
4. Перевірити доступність

Детальні інструкції у розділах нижче.
