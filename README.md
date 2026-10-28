# customer-service

Customer management microservice. Spring Boot, WebFlux, RxJava 3, MongoDB.

## Requirements
- JDK 17, Maven, MongoDB, config-server running on :8888

## Run
1. Start MongoDB and config-server
2. `mvn spring-boot:run`
- Port: 8081 (from config-repo)
- Swagger UI: http://localhost:8081/swagger-ui.html

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | /customers | Create |
| GET | /customers | List |
| GET | /customers/{documentNumber} | Find |
| PUT | /customers/{documentNumber} | Update |
| DELETE | /customers/{documentNumber} | Delete |

## Documentation
See the `docs/` folder: OpenAPI contract, sequence diagram, Postman collection.