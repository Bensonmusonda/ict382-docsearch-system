package com.firstserverapp.serverapp.model;


import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tag_id", columnDefinition = "BINARY(16)")
    private UUID tagId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;


    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Document> documents;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Tag tag = (Tag) o;

        if (tagId != null && tag.tagId != null) {
             return Objects.equals(tagId, tag.tagId);
        }
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {

        if (tagId != null) {
             return Objects.hash(tagId);
        }
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Tag{" +
               "tagId=" + tagId +
               ", name='" + name + '\'' +
               '}';
    }
}
