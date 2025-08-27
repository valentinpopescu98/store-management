# Store Management API

Backend-only REST API for managing products (CRUD). Built with Spring Boot 3 (Java 17), JPA/Hibernate, H2 (dev), and HTTP Basic + RBAC.

---

## Demo credentials & RBAC

| Username | Password   | Roles   | Allowed endpoints                                                  |
| -------: | ---------- | ------- |--------------------------------------------------------------------|
|   `user` | `user123`  | `USER`  | `GET /api/products`, `GET /api/products/{productCode}`             |
|  `admin` | `admin123` | `ADMIN` | All of the above + `POST`, `PATCH`, `DELETE` on `/api/products/**` |

Authentication: **HTTP Basic**.
Unauthorized → **401** (JSON). Forbidden → **403** (JSON).

---

## API

### Endpoints

* `GET /api/products` → list all (200)
* `GET /api/products/{productCode}` → find by product code (200 or 404)
* `POST /api/products` → create (201 or 400)
* `PATCH /api/products/{productCode}/price` → change price (200 or 404/400)
* `DELETE /api/products/{productCode}` → delete (204 or 404)

### RBAC

* `GET` endpoints: **USER** or **ADMIN**
* `POST`, `PATCH`, `DELETE`: **ADMIN** only

### Request/Response examples

**Create**

```http
POST /api/products
Authorization: Basic <user:pass>
Content-Type: application/json
```

```json
{
  "productCode": "abc123",
  "name": "TV",
  "price": 3000.50
}
```

Validation:

* `productCode` required, **unique**
* `name` required
* `price` required, ≥ 0.01

**Change price**

```http
PATCH /api/products/abc123/price
Authorization: Basic <admin:pass>
Content-Type: application/json
```

```json
{ "price": 25.50 }
```

**Delete**

```http
DELETE /api/products/abc123
Authorization: Basic <admin:pass>
```

---

## Error model

All errors follow the same envelope:

```json
{
  "timestamp": "2025-08-27T08:10:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "price must be > 0.01",
  "path": "/api/products"
}
```

Common cases:

* **400** – validation (`MethodArgumentNotValidException` / `DataIntegrityViolationException`)
* **401** – unauthenticated (Basic credentials missing/invalid)
* **403** – authenticated but insufficient role
* **404** – product not found
* **500** – unexpected error (fallback)

---

## Testing

Coverage (examples):

* **Repository**: `@DataJpaTest` (`findByProductCode`, `existsByProductCode`, unique constraint)
* **Service**: unit tests with Mockito (duplicate product code, change price, delete, not found)
* **Controller**: `@WebMvcTest` + `spring-security-test` (RBAC, 401/403, validation 400, happy paths)

---

## Design notes

* DTOs for input/output (`ProductCreateRequest`, `PriceChangeRequest`, `ProductResponse`)
* Unified error envelope (`ApiException`) + field-level `errors[]`
* RBAC via `@PreAuthorize` on controller methods
* H2 for dev; ready to swap to Postgres + Flyway/Testcontainers
* Logging: targeted SQL/debug toggles for dev

---

## Assignment checklist

Create an API that acts as a store management tool:
* Implement basic functions, for example: add-product, find-product, change-price or others
* Optional: Implement a basic authentication mechanism and role based endpoint access
* Design error mechanism and handling plus logging
* Write unit tests, at least for one class
* Nice to have: use Java 17+ features
* Add a small Readme to document the project