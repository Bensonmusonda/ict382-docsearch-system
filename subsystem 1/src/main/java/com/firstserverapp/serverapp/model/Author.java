package com.firstserverapp.serverapp.model;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "authors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "author_id", columnDefinition = "BINARY(16)")
    private UUID authorId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "affiliation", length = 255)
    private String affiliation;

    @Column(name = "email", length = 255)
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Document> documents;

    public Author() {
    }

    public Author(String name, String affiliation, String email) {
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
 
        if (authorId != null && author.authorId != null) {
            return Objects.equals(authorId, author.authorId);
        }
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {

        if (authorId != null) {
            return Objects.hash(authorId);
        }
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Author{" +
               "authorId=" + authorId +
               ", name='" + name + '\'' +
               ", affiliation='" + affiliation + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
