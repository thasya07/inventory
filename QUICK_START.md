# Quick Start - Inventory API

This guide explains how to run the application locally and provides a few common API examples to get you started. It focuses on practical usage with concise examples and notes about validation and expected responses.

## Run the application

Open a terminal and run:

```powershell
cd D:\Project\inventory
.\mvnw.cmd spring-boot:run
```

The server starts at http://localhost:8080.

To build a jar instead:

```powershell
.\mvnw.cmd clean package
java -jar target\inventory-0.0.1-SNAPSHOT.jar
```

Use `API-TESTS.http` in the repository if you prefer a collection of ready-made requests.

## Common requests

Below are a few practical examples. All requests use the base URL http://localhost:8080/api/v1.

1) Create an item (with initial stock)

Request:
```http
POST /items
Content-Type: application/json

{
  "item": {
    "name": "Premium Jeans",
    "sku": "JEANS-002",
    "description": "High-quality denim",
    "basePrice": 79.99
  },
  "stock": {
    "quantity": 50,
    "reserved": 0,
    "reorderLevel": 10
  }
}
```

Response: 201 Created — created item DTO (id, name, sku, basePrice, timestamps).

2) Reserve stock for an item

Request:
```http
POST /items/1/reserve
Content-Type: application/json

{ "quantity": 10 }
```

Response: 200 OK — the response includes the updated stock state (quantity, reserved, available and a timestamp).

3) Deduct stock (finalize sale)

Request:
```http
POST /items/1/deduct
Content-Type: application/json

{ "quantity": 5 }
```

Response: 200 OK — updated stock state returned.

4) Release reserved stock

Request:
```http
POST /items/1/release-reserved
Content-Type: application/json

{ "quantity": 5 }
```

Response: 200 OK — updated stock state returned. If you try to release more than is reserved, the API returns a 400 error explaining the problem.

5) Add stock (restock)

Request:
```http
POST /items/1/add
Content-Type: application/json

{ "quantity": 100 }
```

Response: 200 OK — updated stock state returned.

## Notes and behavior

- All stock-operation requests require a positive integer `quantity`. Requests with non-positive or missing quantities will be rejected with 400 Bad Request.
- Reserve/deduct operations check availability and may return 400 Bad Request if there is insufficient stock.
- Release operations check reserved amounts and will return 400 Bad Request when attempting to release more than reserved.
- The in-memory H2 database is used for convenience; data resets on each restart unless you change the configuration.

## H2 console

Visit the H2 console at http://localhost:8080/h2-console

- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (leave blank)

## What changed compared to the initial version

- Controllers now accept well-defined request DTOs instead of plain JSON maps; this improves validation and type safety.
- Stock endpoints return structured responses with the updated stock state so clients can immediately show correct inventory numbers.
- Validation was added: request bodies are checked for required fields and positive quantities.
- Basic logging (INFO/DEBUG) was added to service methods to make operations easier to trace during testing.

If you'd like, I can also trim the README examples to a single page or add a short OpenAPI/Swagger spec — tell me how you want it presented.

