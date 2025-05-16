<hr style="border: 2px solid black;">

## 🧩 Subsystem 1 - Document Storage (Java/Spring Boot) - README.md 🧩

<hr style="border-top: 1px dashed black;">

**📌 Its Role:** Subsystem 1 serves as the foundational layer for document storage and metadata management. Built with Java and Spring Boot, it provides a robust backend for persistently storing information about users, authors, documents, tags, and document-specific metadata. It utilizes a MySQL database to ensure data integrity and efficient retrieval.

<hr style="border-top: 1px dashed black;">

**🧰 Tech Used:**

* Java
* Spring Boot
* Spring Data JPA (for database interaction)
* MySQL (relational database)

<hr style="border-top: 1px dashed black;">

**💾 Database Schema:**

The subsystem utilizes the following tables in its `subsystem1` MySQL database:

* **`users`:** Stores user information (`user_id` (BINARY(16) PRIMARY KEY), `username` (UNIQUE, NOT NULL), `email` (UNIQUE, NOT NULL), `created_at`).
* **`authors`:** Stores author details (`author_id` (BINARY(16) PRIMARY KEY), `name` (UNIQUE, NOT NULL), `affiliation`, `email`).
* **`documents`:** Stores core document information (`document_id` (BINARY(16) PRIMARY KEY), `title` (NOT NULL), `author_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `authors`), `upload_date`, `user_id` (BINARY(16), FOREIGN KEY referencing `users`), `file_path` (TEXT, NOT NULL), `content` (TEXT)).
* **`metadata`:** Stores key-value pair metadata for documents (`metadata_id` (INT AUTO_INCREMENT PRIMARY KEY), `document_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `documents`), `tag_key` (NOT NULL), `tag_value` (NOT NULL)).
* **`document_rankings`:** Stores ranking scores for documents (`document_id` (BINARY(16) PRIMARY KEY, FOREIGN KEY referencing `documents`), `rank_score` (FLOAT), `last_updated`).
* **`tags`:** Stores unique tags (`tag_id` (BINARY(16) PRIMARY KEY), `name` (UNIQUE, NOT NULL)).
* **`document_tags`:** A junction table linking documents to tags (`document_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `documents`), `tag_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `tags`), PRIMARY KEY (`document_id`, `tag_id`)).

<hr style="border-top: 1px dashed black;">

**🔄 Endpoints:**

* **`/api/documents/`:**
    * `POST`: Create a new document.
    * `GET /{document_id}`: Retrieve details of a specific document by its ID.
* **`/api/authors/`:**
    * `POST`: Create a new author.
    * `GET /{author_id}`: Retrieve details of a specific author by their ID.
    * `GET /`: Retrieve a list of all authors.
* **`/api/tags/`:**
    * `POST`: Create a new tag.
    * `GET /{tag_id}`: Retrieve details of a specific tag by its ID.
    * `GET /`: Retrieve a list of all tags.
* **`/api/users/`:**
    * `POST`: Create a new user.
    * `GET /{user_id}`: Retrieve details of a specific user by their ID.
    * `GET /`: Retrieve a list of all users.
* **`/api/metadata/`:** (Likely endpoints exist for creating, retrieving, and managing document metadata based on the `MetadataController.java`)
* **`/api/uuid/`:** (Likely endpoints exist for generating UUIDs based on the `UUIDController.java`)

<hr style="border-top: 1px dashed black;">

**🏁 How to Run Locally:**

1.  Ensure you have Java (JDK) installed and a MySQL server running.
2.  Create a database named `subsystem1` in your MySQL server.
3.  Configure the database connection properties in the `src/main/resources/application.properties` or `application.yml` file to match your MySQL setup (host, port, username, password). Ensure the driver (`com.mysql.cj.jdbc.Driver`) is correctly specified.
4.  Build the project using Maven (`mvn clean install`) or Gradle (`./gradlew build`) from the `subsystem 1` directory.
5.  Run the application using `mvn spring-boot:run` (Maven) or `./gradlew bootRun` (Gradle). Spring Boot will automatically handle the database schema creation based on the JPA entities.

<hr style="border-top: 1px dashed black;">

**🧪 Testing Approach:**

Testing for Subsystem 1 includes:

* **Unit Tests:** Verifying the logic of individual service classes (e.g., `AuthorService`, `DocumentService`, `MetadataService`, `TagService`, `UserService`).
* **Integration Tests:** Testing the interaction between different components, especially the controllers and repositories, ensuring data is correctly persisted and retrieved from the `subsystem1` MySQL database. Spring's testing framework (`@SpringBootTest`, `@DataJpaTest`) is utilized for these tests.
* **API Endpoint Testing:** Using tools like Postman, `curl`, or a REST client within your IDE to send HTTP requests to the defined API endpoints and verifying the responses (status codes, JSON data format) align with the expected behavior.

<hr style="border-top: 1px dashed black;">

**🚀 Any special algorithms or implementation details:**

Subsystem 1 leverages Spring Data JPA to abstract away much of the boilerplate code for database interactions. The system relies on standard relational database principles for data storage and retrieval. Foreign key constraints (e.g., `author_id` in `documents` referencing `authors.author_id` with `ON DELETE CASCADE`) ensure data integrity. The use of UUIDs (as `BINARY(16)` in MySQL) for primary keys like `user_id`, `author_id`, `document_id`, and `tag_id` ensures global uniqueness. The `document_rankings` table suggests a future feature for ranking documents, although the current implementation details for this are not explicitly shown.

<hr style="border: 2px solid black;">