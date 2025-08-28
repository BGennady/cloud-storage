# Cloud Storage REST API

## Описание проекта
Проект представляет собой **REST-сервис облачного хранилища**.  
Сервис предоставляет REST-интерфейс для:
- авторизации пользователей,
- загрузки файлов,
- вывода списка файлов пользователя,
- удаления файлов.

Все запросы к сервису **авторизованы**. FRONT-приложение подключается к сервису без доработок.

## Технологии
- Java 17
- Spring Boot
- Spring Security (JWT-токены)
- PostgreSQL
- JPA/Hibernate
- Flyway (миграции базы)
- Docker & Docker Compose
- JUnit + Mockito (unit-тесты)
- Testcontainers (интеграционные тесты)
- Maven/Gradle

## Архитектура
- **UserController** — login/logout
- **FilesController** — загрузка, удаление, список файлов
- **FileService** — работа с файлами (диск + база)
- **UserService** — аутентификация и управление токенами
- **Repositories** — JPA репозитории для Users, Files, Tokens
- **JwtTokenFilter** и **TokenFilter** — проверка токенов в запросах
- **application.yml** — все настройки (порт, БД, Flyway)
- **D:/New/** — локальная папка для хранения файлов

## База данных
- PostgreSQL
- Схема: `storage`
- Таблицы: `users`, `files`, `token`
- Инициализация: Flyway миграции
- Примеры пользователей:
  ```sql
  INSERT INTO users (login, password)
  VALUES
  ('admin',  '$2a$10$j9eoahJe/kd6gRNlPEvcIe5ZfvthiaJ6hj0NBrjTGQwiSSg1GP3sq'),
  ('test',  '$2a$10$0Zi3jiOI/Jp1uHjXKJueC.LoxYtYYYUlhrA8spqEeJ1.2wbuulvF'),
  ('guest',  '$2a$10$tHDT9Dm2ien2VAmwZkCEi.HJiBItj9XnjTwvq8uc7bkHU1Sl3ggCu');
