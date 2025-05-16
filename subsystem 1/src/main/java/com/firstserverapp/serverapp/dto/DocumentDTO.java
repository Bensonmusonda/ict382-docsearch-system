package com.firstserverapp.serverapp.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class DocumentDTO {

    private UUID documentId;
    private String title;
    private LocalDateTime uploadDate;
    private String filePath;
    private String content;
    private AuthorDTO author;

    public DocumentDTO(UUID documentId, String title, LocalDateTime uploadDate,
                       String filePath, String content, AuthorDTO author) {
        this.documentId = documentId;
        this.title = title;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        this.content = content;
        this.author = author;
    }

    public UUID getDocumentId() { return documentId; }
    public String getTitle() { return title; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public String getFilePath() { return filePath; }
    public String getContent() { return content; }
    public AuthorDTO getAuthor() { return author; }
}
