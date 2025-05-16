-- Drop databases if they exist (optional, but good for a clean slate)

-- Create and use the target database (e.g., subsystem1)
-- You'll run this block twice, once for subsystem1 and once for subsystem2
CREATE DATABASE subsystem2; -- Or subsystem2
USE subsystem2; -- Or subsystem2

-- Users Table
CREATE TABLE users (
    user_id BINARY(16) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Authors Table
CREATE TABLE authors (
    author_id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    affiliation VARCHAR(255),
    email VARCHAR(255),
    UNIQUE(name)
);

-- Documents Table
CREATE TABLE documents (
    document_id BINARY(16) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id BINARY(16) NOT NULL,
    upload_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BINARY(16),
    file_path TEXT NOT NULL,
    content TEXT,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE, -- Ensure author_id in authors is BINARY(16)
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE      -- Ensure user_id in users is BINARY(16)
);

-- Metadata Table
-- metadata_id can remain INT AUTO_INCREMENT as it's specific to this table and not a shared global ID.
CREATE TABLE metadata (
    metadata_id INT AUTO_INCREMENT PRIMARY KEY,
    document_id BINARY(16) NOT NULL,
    tag_key VARCHAR(100) NOT NULL,
    tag_value VARCHAR(255) NOT NULL,
    FOREIGN KEY (document_id) REFERENCES documents(document_id) ON DELETE CASCADE -- Ensure document_id in documents is BINARY(16)
);

-- Document Rankings Table
CREATE TABLE document_rankings (
    document_id BINARY(16) PRIMARY KEY,
    rank_score FLOAT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(document_id) ON DELETE CASCADE -- Ensure document_id in documents is BINARY(16)
);

-- Tags Table
CREATE TABLE tags (
    tag_id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Document Tags Table (Junction Table)
CREATE TABLE document_tags (
    document_id BINARY(16) NOT NULL,
    tag_id BINARY(16) NOT NULL,
    PRIMARY KEY (document_id, tag_id),
    FOREIGN KEY (document_id) REFERENCES documents(document_id) ON DELETE CASCADE, -- Ensure document_id in documents is BINARY(16)
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE                -- Ensure tag_id in tags is BINARY(16)
);

-- Indexes
-- The existing indexes are fine, MySQL will handle indexing BINARY columns.
CREATE INDEX idx_tag_name ON tags(name);
-- The composite primary key on document_tags already creates an index.
-- CREATE INDEX idx_doc_tag ON document_tags(document_id, tag_id); -- This is redundant if (document_id, tag_id) is PK

-- Optional: Add indexes on foreign key columns if not automatically created by your MySQL version/config and if performance dictates.
-- e.g., CREATE INDEX idx_doc_author_id ON documents(author_id);
-- e.g., CREATE INDEX idx_doc_user_id ON documents(user_id);
-- e.g., CREATE INDEX idx_meta_doc_id ON metadata(document_id);
-- e.g., CREATE INDEX idx_doctag_tag_id ON document_tags(tag_id);