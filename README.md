# 🔐 SecureEchoAPI — Vault Management API

> **"A robust, secure, and scalable API to safeguard sensitive data with encryption, auto-deletion on unauthorized
attempts, and complete user vault lifecycle management."**

---

# 📝 Summary
## 🔐 Secure Vault API

### 📌 Overview
This project is a **secure vault API** that lets users:
- **Register** and create a personal vault.
- **Login** to get access via a secure token.
- **Store and access sensitive data securely** using encryption.
- Automatically **protect data** by deleting the vault after 3 wrong access attempts.

It's built with **Java Spring Boot**, uses **JWT tokens for login security**, and **encrypts data** so no one can read it without the correct secret key.

---

### 🎯 Objective
To provide a simple yet secure system where users can:
- Safely store sensitive information.
- Control access via secret keys.
- Protect data from unauthorized access with automated safeguards.

---

### 🛠️ Key Features
- ✅ **User Sign-Up & Login** with JWT token.
- ✅ **Data Encryption** to keep vault content private.
- ✅ **Key-Based Access**: Only the correct key decrypts the data.
- ✅ **Failsafe Mechanism**: After 3 wrong keys, the vault is deleted.
- ✅ **Simple APIs** to interact with the system.

---

### ✨ Why It's Unique
- Combines **encryption, login security, and auto-protection** of data.
- Even if someone logs in, they still need the secret key to unlock the vault.
- Designed to prevent brute-force attacks by deleting data after repeated failures.

---

### 🚀 Getting Started
1. **Register** via `/api/auth/sign-up`
2. **Login** via `/api/auth/sign-in` to get a secure token.
3. **Access your vault** by providing the correct secret key.
4. Be careful — after 3 wrong key attempts, the data is permanently deleted!

---
# 📋 Details
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

## 🎯 Objective

The goal of this project is to develop a **secure vault API service** that allows users to:
- Register and authenticate via secure JWT-based authentication.
- Store encrypted sensitive data within a personal vault.
- Retrieve and decrypt the vault data securely using a secret key.
- Implement security measures like failed attempt tracking and automatic vault deletion after multiple invalid access attempts.

This ensures a robust API that prioritizes **data security, privacy, and controlled access**.

---

## 🛠️ Use Cases

1. **User Registration**
    - New users can sign up with their credentials and personal details.
    - A personal vault is automatically created for the user.

2. **User Authentication**
    - Users can log in using their credentials and receive a JWT token for secured API access.

3. **Vault Data Access**
    - Authenticated users can securely retrieve and decrypt their vault data using a valid key.
    - Incorrect keys increase the failed attempt count, with automatic vault deletion after 3 failed attempts.

4. **Data Security & Compliance**
    - Sensitive data is encrypted before storage.
    - Vault access is controlled with key verification and limited access attempts to prevent brute-force attacks.

---

This project can be extended for use in:
- Secure document storage APIs
- Password managers
- Confidential data vaults within larger enterprise systems



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
## 🐳 Docker Image

A pre-built Docker image for this application is available on **Docker Hub**.

### ✅ Pull the Image
```bash
docker pull ashishchaudhary9523/secure-echos-rest-api:v2.0
```
> The application is accessible at:
http://localhost:8080

---
## 🔧 Prerequisites
- Java 17+
- Maven
- Docker (optional for containerization)
- Postman or cURL for API testing

---

## 🧹 Enhancements

* JWT authentication for API access.
* Configurable max failed attempts.
* The user can only access vault.
* Audit logs for access and deletion.
* Deployment via Docker.

---
## ✨ Uniqueness

This project stands out due to its **built-in security mechanisms** and **automated safeguards**:

- 🔒 **Vault Encryption**: Sensitive data is securely encrypted before storage using industry-standard encryption algorithms.

- 🔑 **Key-Based Decryption with Security Checks**: Vault data can only be accessed using the correct secret key. Incorrect keys trigger failure tracking.

- 🚨 **Auto-Destruction Policy**: The system automatically deletes a user’s vault after **3 consecutive failed decryption attempts**, minimizing risks from brute-force attacks.

- 🛡️ **JWT-Based Authentication**: Secure, stateless session handling ensures only authenticated users can interact with their vault data.

- 📈 **Integrated Security and Access Tracking**: Tracks the number of failed access attempts, providing both accountability and an additional security layer.

This combination of encryption, access controls, and self-defensive mechanisms makes the project suitable for applications demanding **high levels of data confidentiality and security resilience**.



---

## 👨‍💼 Developed By

* **Ashish Kumar**

---
## 📬 Contact
For questions, contact:
**Ashish Kumar**

GitHub: [ashishchaudhary9523](https://github.com/ashishchaudhary9523)

Linkedin: [Ashish kumar](https://www.linkedin.com/in/ashish-kumar-0333b8373)

Instagram: [Instagram](https://www.instagram.com/active.vicky_?igsh=cTRqZGdrbndyYTVw)

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## 📣 Final Note

> "In a world where data privacy is paramount, SecureEchoAPI delivers trust, reliability, and confidentiality — one
> vault at a time."

---

Feel free to reach out or contribute to this project to make it even better!
