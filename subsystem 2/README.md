<hr style="border: 2px solid black;">

## ⚙️ Subsystem 2 - Document Processing and Storage (Python/FastAPI) - README.md ⚙️

<hr style="border-top: 1px dashed black;">

**📌 Its Role:** Subsystem 2 mirrors the document storage capabilities of Subsystem 1 but is implemented using Python and the FastAPI framework. It is responsible for receiving uploaded documents, extracting textual content from various file types (PDF, DOCX, TXT), saving the original files, and storing document metadata and content within its own MySQL database. It provides API endpoints for managing users, authors, and documents.

<hr style="border-top: 1px dashed black;">

**🧰 Tech Used:**

* Python
* FastAPI (web framework)
* SQLAlchemy (ORM for database interaction)
* PyPDF2 (for PDF text extraction)
* python-docx (for DOCX text extraction)
* MySQL (relational database)

<hr style="border-top: 1px dashed black;">

**💾 Database Schema:**

The subsystem utilizes the following tables in its `subsystem2` MySQL database:

* **`users`:** Stores user information (`user_id` (BINARY(16) PRIMARY KEY), `username` (UNIQUE, NOT NULL), `email` (UNIQUE, NOT NULL), `created_at`).
* **`authors`:** Stores author details (`author_id` (BINARY(16) PRIMARY KEY), `name` (UNIQUE, NOT NULL), `affiliation`, `email`).
* **`documents`:** Stores core document information (`document_id` (BINARY(16) PRIMARY KEY), `title` (NOT NULL), `author_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `authors`), `upload_date`, `user_id` (BINARY(16), FOREIGN KEY referencing `users`), `file_path` (TEXT, NOT NULL), `content` (TEXT)).
* **`metadata`:** Stores key-value pair metadata for documents (`metadata_id` (INT AUTO_INCREMENT PRIMARY KEY), `document_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `documents`), `tag_key` (NOT NULL), `tag_value` (NOT NULL)).
* **`document_rankings`:** Stores ranking scores for documents (`document_id` (BINARY(16) PRIMARY KEY, FOREIGN KEY referencing `documents`), `rank_score` (FLOAT), `last_updated`).
* **`tags`:** Stores unique tags (`tag_id` (BINARY(16) PRIMARY KEY), `name` (UNIQUE, NOT NULL)).
* **`document_tags`:** A junction table linking documents to tags (`document_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `documents`), `tag_id` (BINARY(16) NOT NULL, FOREIGN KEY referencing `tags`), PRIMARY KEY (`document_id`, `tag_id`)).

<hr style="border-top: 1px dashed black;">

**🔄 Endpoints:**

* `GET /api/documents/allcontent`: Returns a list of all document IDs and their extracted content.
* `GET /api/users/{user_id}`: Retrieves information for a specific user.
* `GET /api/users/`: Retrieves a list of all users.
* `POST /api/users/`: Creates a new user.
* `GET /api/documents/{document_id}`: Retrieves details of a specific document.
* `POST /api/documents/`: Accepts a multipart form with `title`, `author_id`, `user_id`, and a `file` upload. It extracts the text content, saves the file to the `uploaded_documents` directory, and creates a new document record in the database.
* `GET /api/authors/{author_id}`: Retrieves information for a specific author.
* `GET /api/authors/`: Retrieves a list of all authors.
* `POST /api/authors/`: Creates a new author.

<hr style="border-top: 1px dashed black;">

**🏁 How to Run Locally:**

1.  Ensure you have Python 3.7+ installed.
2.  Navigate to the `subsystem 2` directory.
3.  Create a virtual environment (recommended): `python -m venv venv` and activate it (`source venv/bin/activate` on Linux/macOS, `.\venv\Scripts\activate` on Windows).
4.  Install dependencies: `pip install -r requirements.txt`.
5.  Ensure you have a MySQL server running. Create a database named `subsystem2`.
6.  Configure the database connection details in `app/database.py` (e.g., host, port, username, password, database name).
7.  Run the FastAPI application using `uvicorn app.main:app --reload`. SQLAlchemy will handle the creation of the tables based on the defined models in `app/models.py`.

<hr style="border-top: 1px dashed black;">

**🧪 Testing Approach:**

Testing for Subsystem 2 likely involves:

* **Unit Tests:** Verifying the functionality of core modules like `app/crud.py` (database operations) and the text extraction logic within `main.py`.
* **Integration Tests:** Testing the API endpoints using tools like `httpx`, `requests`, or Postman to ensure they correctly handle HTTP requests, interact with the `subsystem2` MySQL database, and return the expected JSON responses. This includes testing file uploads and the creation of document records. You might want to create test cases for different file types to ensure the extraction works as expected.

<hr style="border-top: 1px dashed black;">

**🚀 Any special algorithms or implementation details:**

Subsystem 2 implements specific logic within the `extract_text_from_upload_file` function in `main.py` to handle the extraction of text content from different document formats (`.pdf` using PyPDF2, `.docx` using python-docx, and `.txt` using standard file reading). Uploaded files are saved to the `uploaded_documents` directory on the server's file system. The `app/crud.py` module provides functions that use SQLAlchemy to interact with the `subsystem2` database, performing CRUD operations on the defined models (`app/models.py`). The routes defined in `main.py` handle incoming API requests and utilize the CRUD functions and text extraction logic.

<hr style="border: 2px solid black;">