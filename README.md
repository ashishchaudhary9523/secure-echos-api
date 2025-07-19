# 🔐 SecureEchoAPI — Vault Management API

> **"A robust, secure, and scalable API to safeguard sensitive data with encryption, auto-deletion on unauthorized
attempts, and complete user vault lifecycle management."**

---

## 🚀 Overview

SecureEchoAPI is a **Java Spring Boot** REST API designed to securely store, retrieve, and manage sensitive data. Built
with security-first principles, the system provides:

* **AES-256 Encryption** for securing data.
* **BCrypt Hashing** for vault access keys.
* **Auto-deletion of Vault and Customer data after 3 failed access attempts.**
* **Transactional integrity** to ensure consistency across data operations.

This makes SecureEchoAPI perfect for scenarios like:

* Secure sharing of confidential documents.
* One-time sensitive information delivery.
* Temporary secret storage.

---

## 🏗️ Architecture

```
Client <--> VaultController (REST API)
            |
            v
    VaultServiceImplementation
            |
     ---------------------
     |                   |
CustomerRepository  VaultRepository
            |
            v
   Database (MySQL/Postgres/etc.)
```

---

## ⚙️ Core Features

| Feature                     | Description                                                         |
|-----------------------------|---------------------------------------------------------------------|
| 🔒 **Data Encryption**      | Data is AES-256 encrypted before storage.                           |
| 🔑 **Password Security**    | Keys are hashed using BCrypt to prevent plaintext storage.          |
| 🔄 **Transactional Safety** | Deletion and updates occur within transactions to ensure integrity. |
| ✅ **One-Time Access**       | Once decrypted successfully, data is deleted to ensure secrecy.     |


---

## 💪 Tech Stack

| Technology      | Purpose                  |
|-----------------|--------------------------|
| Java 21+        | Core Language            |
| Spring Boot     | Backend framework        |
| Spring Data JPA | ORM for DB interactions  |
| Hibernate       | Persistence provider     |
| MySQL/Postgres  | Database                 |
| Lombok          | Reduces boilerplate code |
| AES Encryption  | Encrypting data at rest  |
| BCrypt          | Hashing user keys        |

---
### 📌 Auth API Endpoints

### 🔐 `Sign Up (Register)`
* Endpoint:
* POST /api/auth/sign-up

* Description:
* Register a new user with a vault initialized with dummy data.

* **Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "userName": "john_doe",
  "password": "securePassword"
}

```
* **Success Response:**

```successfully sign-up!```


* **Error Response:**
  `400` Bad Request.

---

### 🔐 `Sign In (Login)`
* Endpoint:
* POST /api/auth/sign-in

* Description:
* Authenticate a user and receive a JWT token.

* **Request Body:**
```json
{
  "userName": "exampleUser",
  "password": "examplePassword"
  }
```
* **Response Example:**
 
```json
{
  "accessToken": "jwt_token_here",
  "tokenType": "Bearer"
}
```

* **Error Response:**
  `401` Unauthorized if credentials are invalid.





---

## 🔗 API Endpoints

### ➡️ `POST /vault/store-data`

* **Purpose:** Store a new encrypted vault for a user.
* **Request Body:**

```json
{
  "userName": "john_doe",
  "encryptedData": "Sensitive Information",
  "key": "secureKey123"
}
```

* **Response:**
  `200 OK` → `Data saved`

---

### ➡️ `POST /vault/get-data`

* **Purpose:** Retrieve and decrypt stored vault data.

* **Behavior:**

    * On correct key → returns decrypted data and deletes vault + customer.
    * On incorrect key → increments `failedAttempts`.
    * On 3 failed attempts → deletes vault and customer.

* **Request Body:**

```json
{
  "userName": "john_doe",
  "key": "secureKey123"
}
```

* **Response:**

    * ✅ Success: Decrypted data.
    * ❌ Fail: Error with remaining attempts or vault deletion message.

---

## 🔒 Security Workflow

1. **Vault Creation**

    * Data is encrypted using AES.
    * The key is hashed via BCrypt.
    * Stored alongside the customer record.

2. **Data Access**

    * Key is matched via BCrypt.
    * On success → data is decrypted and vault + customer are deleted.
    * On failure → `failedAttempts` incremented.
    * After 3 failed attempts → auto-deletion.

---

## 🧰 Database Model

### Customer

| Field    | Type   | Notes               |
|----------|--------|---------------------|
| userName | String | Primary Key, Unique |
| email    | String | Unique, Not null    |
| name     | String | Not null            |
| password | String | Not null            |

### Vault

| Field          | Type   | Notes                            |
|----------------|--------|----------------------------------|
| userName       | String | Primary Key, FK to Customer      |
| encryptedData  | String | Encrypted vault data             |
| key            | String | BCrypt hash of the vault key     |
| failedAttempts | int    | Counter for invalid key attempts |

---

## 🔄 Relationships

* **Customer 1:1 Vault**
* When a vault is deleted, its corresponding customer is also deleted ensuring **data hygiene**.

---

## ✅ Setup Instructions

1. **Clone the Repo**

```bash
git clone https://github.com/ashishchaudhary9523/SecureEchoAPI.git
cd SecureEchoAPI
```

2. **Configure Database**
   Update `application.properties`:

```properties
spring.datasource.url=your_database_url
spring.datasource.username=your_database_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. **Run the Application**

```bash
./mvnw spring-boot:run
```

4. **API Testing**
   Use Postman or Curl to test the endpoints.

---

## 🧹 Enhancements

* JWT authentication for API access.
* Configurable max failed attempts.
* The user can only access vault.
* Audit logs for access and deletion.
* Deployment via Docker.

---

## 👨‍💼 Developed By

* **Ashish Kumar**
* [Github](https://github.com/ashishchaudhary9523)
* [Linkedin](https://www.linkedin.com/in/ashish-kumar-0333b8373)
* [Instagram](https://www.instagram.com/active.vicky_?igsh=cTRqZGdrbndyYTVw)

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## 📣 Final Note

> "In a world where data privacy is paramount, SecureEchoAPI delivers trust, reliability, and confidentiality — one
> vault at a time."

---

Feel free to reach out or contribute to this project to make it even better!
