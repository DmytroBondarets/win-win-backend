# WinWin Backend Mini Test

This workspace contains two separate Spring Boot projects:

- `auth-api` — handles user registration/login, JWT auth, and protected processing endpoint.
- `data-api` — validates an internal shared header and applies the transformation.
- `docker-compose.yml` — launches Postgres, `auth-api`, and `data-api` together.

## Project structure

```text
winwin-backend-test-task/
  auth-api/
    src/
    .mvn/
    mvnw.cmd
    pom.xml
    Dockerfile
  data-api/
    src/
    .mvn/
    mvnw.cmd
    pom.xml
    Dockerfile
  docker-compose.yml
  README.md
```

## Local build

```bash
# auth-api
.\auth-api\mvnw.cmd clean package -DskipTests

# data-api
.\data-api\mvnw.cmd clean package -DskipTests
```

## Run with Docker Compose

```bash
docker compose up -d --build
```

Once running:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"a@a.com","password":"pass"}'

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"a@a.com","password":"pass"}'
```

Save the JWT token returned by login and then call the protected endpoint:

```bash
curl -X POST http://localhost:8080/api/process \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"text":"hello"}'
```

Expected response:

```json
{ "result": "OLLEH" }
```

The app stores a processing log row in PostgreSQL containing the user, input, output, and timestamp.

## Environment variables

- `POSTGRES_URL` (default: `jdbc:postgresql://localhost:5432/authdb`)
- `POSTGRES_USER` (default: `postgres`)
- `POSTGRES_PASSWORD` (default: `postgres`)
- `JWT_SECRET` (default: `change-me-super-secret-key-1234567890`)
- `DATA_API_URL` (default: `http://localhost:8081`)
- `INTERNAL_TOKEN` (default: `winwin-internal-secret`)

## Notes

- Passwords are hashed with BCrypt.
- `auth-api` calls `data-api` using the `X-Internal-Token` header.
- `data-api` rejects any request without the valid shared token with HTTP 403.
