# Cloud Storage — Быстрый старт

## Настройка и запуск проекта

### Backend
1. Установить PostgreSQL и создать базу `cloud_db`.
2. В файле `application.yml` указать настройки базы и Flyway:

```yaml
spring.application.name=cloud-storage
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/cloud_db
spring.datasource.username=postgres
spring.datasource.password=pass
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.schemas=storage
spring.flyway.default-schema=storage
spring.flyway.baseline-on-migrate=true

logging.level.root=DEBUG
logging.level.ru.netology.cloud_storage=DEBUG

# Сборка проекта
mvn clean package

# Запуск через Docker Compose
docker-compose up

# API доступен по адресу
http://localhost:8080/cloud/...

# Настройка Frontend
# Установить Node.js ≥19.7.0
cd FRONT
npm install

# В .env указать URL backend
VUE_APP_BASE_URL=http://localhost:8080

# Запуск фронта
npm run serve

# Доступ через браузер
http://localhost:8080 (или следующий доступный порт)

# Примечания
# FRONT использует Authorization: Bearer <token> для всех запросов
# Для логина используется /cloud/login, для выхода /cloud/logout
# Файлы сохраняются на диск в D:/New/
