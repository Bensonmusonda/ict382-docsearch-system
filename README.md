
# DocSearch System (ICT382 Coursework)

## 🧩 Project Objective
This project, "Document Storage and Search System," aims to develop a multi-component system for managing, storing, and efficiently searching documents. It comprises three distinct subsystems that collaborate to provide robust document handling, storage redundancy/variety (with Java and Python backends for storage), and advanced search capabilities with relevance ranking. The system emphasizes smooth API-driven communication between its components.

[Source: MSc_CourseWork_ICT382_2025.pdf, Project Overview]

## 🏗 System Architecture
The system is designed with three primary subsystems:

1.  **Subsystem 1: Document Storage & Metadata API (Java & MySQL)**
    * Responsible for allowing users to upload and store documents (e.g., text, PDFs).
    * Stores document file paths and associated metadata (title, author, upload date, etc.) in a MySQL database.
    * Provides a RESTful API for document retrieval based on metadata.
    * Implemented using Java, Spring Boot, and **Gradle** for build management.
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 1 Detailed Specifications & User Correction]

2.  **Subsystem 2: Document Storage & Metadata API (Python & MySQL)**
    * Functionally similar to Subsystem 1, this subsystem also handles document uploads and storage.
    * It is implemented using Python (with a web framework like FastAPI or Flask) and MySQL.
    * Provides a RESTful API for retrieving document metadata or content.
    * The actual implementation, as per the provided code snippets, uses FastAPI for the API and also includes text extraction capabilities for uploaded files (PDF, DOCX, TXT) and stores the physical files.
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 2 Detailed Specifications & User-provided code context]

3.  **Subsystem 3: Aggregation, Search, and Ranking API (Python)**
    * Aggregates document data (content and metadata) by pulling from the APIs of both Subsystem 1 and Subsystem 2.
    * Builds a searchable index (initially described as an inverted index, but the provided code for `subsystem 3` uses FAISS for vector embeddings and similarity search) to enable fast full-text search.
    * Implements a ranking function to order search results by relevance (e.g., using TF-IDF, BM25, or cosine similarity with vector embeddings).
    * Provides a RESTful API for users to search documents using keywords.
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 3 Detailed Specifications & User-provided code context]

4.  **Interface: Frontend User Interface (HTML, CSS, JavaScript)**
    * A web-based interface allowing users to submit search queries.
    * Fetches and displays ranked search results by interacting with Subsystem 3's search API.

[Source: MSc_CourseWork_ICT382_2025.pdf, Project Overview & Subsystem descriptions]

## 🧱 Tech Stack

* **Subsystem 1 (Document Storage):**
    * Language: Java (Version 17+ indicated by Spring Boot typical requirements)
    * Framework: Spring Boot
    * Database: MySQL
    * API: RESTful
    * Build Tool: **Gradle**
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 1 Specifications & User Correction]
* **Subsystem 2 (Document Storage):**
    * Language: Python (3.8+)
    * Framework: FastAPI (as per provided code snippets)
    * Database: MySQL
    * API: RESTful
    * File Handling: PyPDF2, python-docx (for text extraction from PDF, DOCX)
    * ORM: SQLAlchemy (inferred from `database.py`, `crud.py`)
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 2 Specifications & User-provided code context]
* **Subsystem 3 (Search & Ranking):**
    * Language: Python (3.8+)
    * Framework: FastAPI
    * Indexing: FAISS (for vector similarity search, as per provided code)
    * Text Embedding: Sentence-Transformers (inferred from `vectorizer.model` usage)
    * API: RESTful
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Subsystem 3 Specifications & User-provided code context. Note: Coursework mentions inverted index, implementation uses vector search.]
* **Frontend Interface:**
    * HTML, CSS, Vanilla JavaScript
* **Version Control:**
    * Git & GitHub

## 🔁 Setup Instructions

### Prerequisites
* Java JDK (version 17 or higher recommended for modern Spring Boot)
* **Gradle**
* Python (version 3.8 or higher)
* Pip (Python package installer)
* Git
* MySQL Server

### General Setup
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/Bensonmusonda/ict382-docsearch-system.git](https://github.com/Bensonmusonda/ict382-docsearch-system.git)
    cd ict382-docsearch-system
    ```
2.  **Database Setup (MySQL):**
    * Ensure your MySQL server is running.
    * Create the necessary databases and tables for Subsystem 1 and Subsystem 2. SQL scripts might be provided in `subsystem 1/src/main/resources` or `subsystem 2/schemas.sql`.
        * **Subsystem 1:** The `documents` table (doc_id, title, author, upload_date, file_path) and potentially a `content` table are required.
        * **Subsystem 2:** Similar schema to Subsystem 1 (`documents` and `content` tables). The provided `subsystem 2/schemas.sql` should define this.
    * Configure database connection details:
        * For Subsystem 1: in `subsystem 1/src/main/resources/application.properties` or `application.yml`.
        * For Subsystem 2: likely in `subsystem 2/app/database.py` or via environment variables.

[Source: MSc_CourseWork_ICT382_2025.pdf, Database Schema]

### Subsystem Setup
Detailed setup instructions are available in the README file of each respective subsystem and the interface directory.
* **Subsystem 1 (Java/Spring Boot):** Navigate to `subsystem 1/`, then build with Gradle (`./gradlew build` or `gradlew.bat build`) and run.
* **Subsystem 2 (Python/FastAPI):** Navigate to `subsystem 2/`, create/activate a Python virtual environment, install dependencies (`pip install -r requirements.txt`), and run with Uvicorn.
* **Subsystem 3 (Python/FastAPI):** Navigate to `subsystem 3/`, create/activate a Python virtual environment, install dependencies (`pip install -r requirements.txt`), and run with Uvicorn.

## 🧪 How to Run & Test It

1.  **Start Subsystem 1 (Java Document Storage API):**
    * Directory: `subsystem 1/`
    * Command: `./gradlew bootRun` (or `gradlew.bat bootRun`, or run the `ServerAppApplication.java` from an IDE).
    * Default Port: Typically `8080` (configurable).

2.  **Start Subsystem 2 (Python Document Storage API):**
    * Directory: `subsystem 2/`
    * Setup: `python -m venv venv && source venv/bin/activate && pip install -r requirements.txt` (or `venv\Scripts\activate` on Windows).
    * Command: `uvicorn app.main:app --reload --port 8001` (Port 8001 is an example).
    * Note: This subsystem, per your code, handles file uploads and text extraction.

3.  **Start Subsystem 3 (Python Search & Ranking API):**
    * Directory: `subsystem 3/`
    * Setup: `python -m venv venv && source venv/bin/activate && pip install -r requirements.txt`
    * Command: `uvicorn app.main:app --reload --port 8002` (Port 8002 is an example).
    * Initial Indexing: After starting, and once Subsystems 1 & 2 have some data, you will likely need to trigger the `POST /admin/reindex` endpoint on Subsystem 3 to build the search index. Check `GET /admin/status` for index readiness.

4.  **Launch the Frontend Interface:**
    * Directory: `interface/`
    * Action: Open `index.html` in a web browser.
    * Configuration: Ensure the JavaScript (`scripts/` or `src/`) correctly points to Subsystem 3's search API endpoint (e.g., `http://localhost:8002/search`).

### Testing
* **Unit Tests:**
    * Subsystem 1: Run with `./gradlew test` (or `gradlew.bat test`).
    * Subsystem 2 & 3: Python's `unittest` or `pytest` (e.g., `subsystem 2/test.py`). Focus on API endpoints, data validation, and core logic like text extraction (Subsystem 2) or indexing/ranking (Subsystem 3).
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Testing sections]
* **Integration Tests:**
    * Verify Subsystem 3 can pull data from Subsystem 1 and Subsystem 2 APIs.
    * Test end-to-end search functionality: upload via Subsystem 1/2 -> reindex on Subsystem 3 -> search via Frontend/Subsystem 3 API.
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Integration Testing]
* **API Testing:** Use tools like Postman, Insomnia, or `curl` for all backend APIs.
* **Performance Testing (as per coursework):**
    * Measure search response time with increasing data.
    * Test scalability.
    * [Source: MSc_CourseWork_ICT382_2025.pdf, Performance Testing]

## 📁 Folder Structure Summary

```text
.
├── interface/                # Frontend (HTML, CSS, JS)
│   ├── index.html
│   ├── scripts/
│   ├── src/
│   └── stylesheets/
├── subsystem 1/              # Document Storage & Metadata API (Java/Spring Boot & MySQL)
│   ├── src/
│   │   └── main/
│   │       ├── java/com/firstserverapp/serverapp/
│   │       │   ├── ServerAppApplication.java
│   │       │   ├── WebConfig.java
│   │       │   ├── config/
│   │       │   ├── controller/ (AuthorController, DocumentController, etc.)
│   │       │   ├── dto/ (AuthorDTO, DocumentDTO)
│   │       │   ├── model/ (Author, Document, Metadata, Tag, User)
│   │       │   ├── projection/ (DocumentSnippet)
│   │       │   ├── repository/ (AuthorRepository, DocumentRepository, etc.)
│   │       │   └── service/ (AuthorService, DocumentService, etc.)
│   │       └── resources/    # application.properties, static, templates
│   ├── build.gradle          # Gradle build file (or build.gradle.kts)
│   ├── gradlew               # Gradle wrapper script (Linux/macOS)
│   ├── gradlew.bat           # Gradle wrapper script (Windows)
│   └── settings.gradle       # Gradle settings file
├── subsystem 2/              # Document Storage & File Handling API (Python/FastAPI & MySQL)
│   ├── app/
│   │   ├── __init__.py
│   │   ├── crud.py           # Database interaction logic
│   │   ├── database.py       # SQLAlchemy setup, SessionLocal
│   │   ├── main.py           # FastAPI app, endpoints for upload, users, authors
│   │   ├── models.py         # SQLAlchemy models
│   │   ├── routers/          # (If using FastAPI routers for modularity)
│   │   └── schemas.py        # Pydantic schemas for API I/O
│   ├── uploaded_documents/   # Directory for storing raw uploaded files
│   ├── requirements.txt      # Python dependencies
│   ├── schemas.sql           # SQL DDL for this subsystem's tables
│   ├── test.py               # Automated tests for this subsystem
│   └── venv/                 # Python virtual environment (usually .gitignored)
├── subsystem 3/              # Aggregation, Search & Ranking API (Python/FastAPI)
│   ├── app/
│   │   ├── __init__.py
│   │   ├── crud.py           # Logic for aggregating data & searching
│   │   ├── index_manager.py  # Manages FAISS index lifecycle (build, load, save)
│   │   ├── main.py           # FastAPI app, /search, /admin/reindex endpoints
│   │   ├── models.py         # (If any specific models for this subsystem)
│   │   ├── schemas.py        # Pydantic schemas (SearchQuery, SearchResultItem)
│   │   └── vectorizer.py     # Handles text vectorization (sentence embeddings)
│   ├── document_id_map.pkl   # Persisted mapping of FAISS indices to document IDs
│   ├── document_index.faiss  # Persisted FAISS index file
│   ├── requirements.txt      # Python dependencies
│   └── venv/                 # Python virtual environment (usually .gitignored)
├── .gitignore
└── README.md                 # This file (Main Project README)
````

## 🙋‍♂️ Who built it

This project was created by Benson Musonda. ([GitHub Profile: Bensonmusonda](https://github.com/Bensonmusonda))