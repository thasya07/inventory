# Warehouse Inventory Management System

Spring Boot REST API for managing warehouse items, variants, and stock levels.

## How to Run

### Prerequisites
- Java 17 or newer

### Start the application
```bash
cd D:\Project\inventory
.\mvnw.cmd spring-boot:run
```

The app starts at `http://localhost:8080`.

### Alternative build and run
```bash
cd D:\Project\inventory
.\mvnw.cmd clean package
java -jar target\inventory-0.0.1-SNAPSHOT.jar
```

### Helpful local URLs
- API base URL: `http://localhost:8080/api/v1`
- H2 console: `http://localhost:8080/h2-console`
- H2 JDBC URL: `jdbc:h2:mem:testdb`
- H2 username: `sa`
- H2 password: leave blank

## Sample Data

The application seeds sample data automatically on startup through `DataInitializer`.

- 3 items: `TSHIRT-001`, `JEANS-001`, `SHOES-001`
- 6 item variants
- Initial stock and reorder levels for both items and variants

This makes the API testable immediately after startup.

## Design Decisions

### 1. Layered architecture
The code is split into controllers, services, repositories, DTOs, mappers, and entities. Business rules stay in the service layer so controllers remain thin and persistence logic stays isolated.

### 2. Separate stock records
Stock is stored separately from item and variant records. This keeps inventory-specific fields such as `quantity`, `reserved`, and `reorderLevel` focused in their own models and makes stock operations easier to reason about.

### 3. Variant support
Items and item variants are modeled separately so the system can handle products like a shirt with multiple sizes or colors, each with its own price and stock quantity.

### 4. In-memory database for easy evaluation
H2 is used so the project can run with no external database setup. That keeps the project easy to review and quick to start for an assignment/demo environment.

### 5. Transaction-based stock updates
Reserve, deduct, release, and add operations belong in the service layer so inventory changes stay consistent and easier to protect with transactions.

## Assumptions

- The system manages a single warehouse.
- Stock is tracked in whole-number units.
- SKU values are unique per item.
- Authentication and authorization are out of scope.
- H2 in-memory storage is acceptable for demo purposes, so data resets on restart.
- Item-level stock and variant-level stock are both useful for this assignment's inventory workflows.

## API Endpoint Examples

Base URL:

```text
http://localhost:8080/api/v1
```

### Table of Contents
- [Item Management](#item-management)
- [Item Variants](#item-variants)
- [Stock Management](#stock-management)
- [Variant Stock Management](#variant-stock-management)
- [Workflow Examples](#workflow-examples)

---

### Item Management

#### Create an item with starting stock
```http
POST /api/v1/items
Content-Type: application/json

{
  "item": {
    "name": "Wireless Headphones",
    "sku": "HEAD-001",
    "description": "Premium wireless headphones",
    "basePrice": 149.99
  },
  "stock": {
    "quantity": 75,
    "reserved": 0,
    "reorderLevel": 20
  }
}
```

#### Get all items
```http
GET /api/v1/items
```

#### Get item by ID
```http
GET /api/v1/items/1
```

#### Get item by SKU
```http
GET /api/v1/items/sku/TSHIRT-001
```

#### Update item
```http
PUT /api/v1/items/1
Content-Type: application/json

{
  "name": "Classic Cotton T-Shirt",
  "basePrice": 24.99
}
```

#### Check if item is in stock
```http
GET /api/v1/items/1/in-stock
```

#### Reserve stock for an item
```http
POST /api/v1/items/1/reserve
Content-Type: application/json

{
  "quantity": 10
}
```

#### Release reserved stock
```http
POST /api/v1/items/1/release-reserved
Content-Type: application/json

{
  "quantity": 5
}
```

#### Deduct stock (actual sale/consumption)
```http
POST /api/v1/items/1/deduct
Content-Type: application/json

{
  "quantity": 3
}
```

#### Add stock (restock/receive shipment)
```http
POST /api/v1/items/1/add
Content-Type: application/json

{
  "quantity": 25
}
```

#### Delete item
```http
DELETE /api/v1/items/1
```

---

### Item Variants

#### Create a variant for an item
```http
POST /api/v1/items/1/variants
Content-Type: application/json

{
  "variant": {
    "name": "Black Large T-Shirt",
    "color": "Black",
    "size": "L",
    "description": "Large size in black",
    "price": 19.99
  },
  "stock": {
    "quantity": 35,
    "reserved": 0,
    "reorderLevel": 8
  }
}
```

#### Get all variants of an item
```http
GET /api/v1/items/1/variants
```

#### Get specific variant
```http
GET /api/v1/items/1/variants/1
```

#### Update variant
```http
PUT /api/v1/items/1/variants/1
Content-Type: application/json

{
  "name": "Updated Variant Name",
  "price": 21.99
}
```

#### Check if variant is in stock
```http
GET /api/v1/items/1/variants/1/in-stock
```

#### Reserve variant stock
```http
POST /api/v1/items/1/variants/1/reserve
Content-Type: application/json

{
  "quantity": 5
}
```

#### Release reserved variant stock
```http
POST /api/v1/items/1/variants/1/release-reserved
Content-Type: application/json

{
  "quantity": 2
}
```

#### Deduct variant stock
```http
POST /api/v1/items/1/variants/1/deduct
Content-Type: application/json

{
  "quantity": 2
}
```

#### Add variant stock
```http
POST /api/v1/items/1/variants/1/add
Content-Type: application/json

{
  "quantity": 15
}
```

#### Delete variant
```http
DELETE /api/v1/items/1/variants/1
```

---

### Stock Management

#### Get all item stocks
```http
GET /api/v1/stocks
```

#### Get stock by ID
```http
GET /api/v1/stocks/1
```

#### Get stock for a specific item
```http
GET /api/v1/stocks/item/1
```

#### Update stock details (quantity and reorder level)
```http
PUT /api/v1/stocks/1
Content-Type: application/json

{
  "quantity": 200,
  "reorderLevel": 30
}
```

#### Get low-stock items (below reorder level)
```http
GET /api/v1/stocks/alerts/low-stock
```

#### Get out-of-stock items (zero available quantity)
```http
GET /api/v1/stocks/alerts/out-of-stock
```

---

### Variant Stock Management

#### Get all variant stocks
```http
GET /api/v1/variant-stocks
```

#### Get variant stock by ID
```http
GET /api/v1/variant-stocks/1
```

#### Get stock for a specific variant
```http
GET /api/v1/variant-stocks/variant/1
```

#### Update variant stock details
```http
PUT /api/v1/variant-stocks/1
Content-Type: application/json

{
  "quantity": 100,
  "reorderLevel": 15
}
```

#### Get low-stock variants
```http
GET /api/v1/variant-stocks/alerts/low-stock
```

#### Get out-of-stock variants
```http
GET /api/v1/variant-stocks/alerts/out-of-stock
```

---

### Workflow Examples

#### Example 1: Complete Order Fulfillment Workflow

**Step 1: Check if item is in stock**
```http
GET /api/v1/items/1/in-stock
```

**Step 2: Reserve stock for the order**
```http
POST /api/v1/items/1/reserve
Content-Type: application/json

{
  "quantity": 5
}
```

**Step 3: Check current stock details**
```http
GET /api/v1/stocks/item/1
```

**Step 4: Deduct stock when order is confirmed**
```http
POST /api/v1/items/1/deduct
Content-Type: application/json

{
  "quantity": 5
}
```

#### Example 2: Handle Order Cancellation

**Step 1: Release the reserved stock**
```http
POST /api/v1/items/1/release-reserved
Content-Type: application/json

{
  "quantity": 5
}
```

#### Example 3: Receive New Shipment

**Step 1: Check current stock**
```http
GET /api/v1/stocks/item/1
```

**Step 2: Add new stock from shipment**
```http
POST /api/v1/items/1/add
Content-Type: application/json

{
  "quantity": 100
}
```

**Step 3: Verify new stock level**
```http
GET /api/v1/stocks/item/1
```

#### Example 4: Monitor Low Stock Alerts

**Get all items that are running low on stock**
```http
GET /api/v1/stocks/alerts/low-stock
```

**Get all out-of-stock items**
```http
GET /api/v1/stocks/alerts/out-of-stock
```

**Get all low-stock variants**
```http
GET /api/v1/variant-stocks/alerts/low-stock
```

#### Example 5: Manage Item Variants

**Step 1: Create a new variant for an item**
```http
POST /api/v1/items/1/variants
Content-Type: application/json

{
  "variant": {
    "name": "Red Medium T-Shirt",
    "color": "Red",
    "size": "M",
    "description": "Medium size in red",
    "price": 19.99
  },
  "stock": {
    "quantity": 50,
    "reserved": 0,
    "reorderLevel": 10
  }
}
```

**Step 2: Get all variants and their stock levels**
```http
GET /api/v1/items/1/variants
```

**Step 3: Manage variant stock (reserve, deduct, add)**
```http
POST /api/v1/items/1/variants/1/reserve
Content-Type: application/json

{
  "quantity": 3
}
```

---

For a complete request collection with all endpoints and additional test cases, see [`API-TESTS.http`](./API-TESTS.http).
