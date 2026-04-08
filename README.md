# MtsRoutingProject

Spring Boot приложение с маршрутизацией запросов между тремя базами данных PostgreSQL.

## Технологии

- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL 16
- Flyway (миграции)
- Springdoc OpenAPI (Swagger UI)
- Docker / Docker Compose

## Требования

- Docker >= 20.10
- Docker Compose >= 2.0

## Запуск

### 1. Клонировать репозиторий

```bash
git clone <url>
cd MtsRoutingProject
```

### 2. Запустить через Docker Compose

```bash
docker compose up --build
```

Compose автоматически:
- Соберёт образ приложения из `Dockerfile`
- Поднимет три инстанса PostgreSQL
- Дождётся готовности всех баз данных
- Запустит Spring Boot приложение

### 3. Проверить что приложение запустилось

```bash
curl http://localhost:8080/actuator/health
```

### 4. Открыть Swagger UI
```
http://localhost:8080/swagger-ui.html
```