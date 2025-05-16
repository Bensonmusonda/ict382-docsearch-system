package com.firstserverapp.serverapp.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id", columnDefinition = "BINARY(16)")
    private UUID documentId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private Author author;

    @Column(name = "upload_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "file_path", nullable = false, columnDefinition = "TEXT")
    private String filePath;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;


    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Metadata> metadataEntries = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "document_tags",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    protected void onUpload() {
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }


    public Document() {
    }

    public Document(String title, Author author, User user, String filePath, String content) {
        this.title = title;
        this.author = author;
        this.user = user;
        this.filePath = filePath;
        this.content = content;
    }

 
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getDocuments().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getDocuments().remove(this);
    }

    public void addMetadata(Metadata meta) {
        this.metadataEntries.add(meta);
        meta.setDocument(this);
    }

    public void removeMetadata(Metadata meta) {
        this.metadataEntries.remove(meta);
        meta.setDocument(null);
    }


    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Metadata> getMetadataEntries() {
        return metadataEntries;
    }

    public void setMetadataEntries(Set<Metadata> metadataEntries) {
        this.metadataEntries = metadataEntries;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(documentId, document.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId);
    }

    @Override
    public String toString() {
        return "Document{" +
               "documentId=" + documentId +
               ", title='" + title + '\'' +
               ", authorId=" + (author != null ? author.getAuthorId() : null) +
               ", uploadDate=" + uploadDate +
               ", userId=" + (user != null ? user.getUserId() : null) +
               ", filePath='" + filePath + '\'' +
               '}';
    }
}
