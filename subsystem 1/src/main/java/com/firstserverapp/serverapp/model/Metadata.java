package com.firstserverapp.serverapp.model;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "metadata")
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metadata_id")
    private Integer metadataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "tag_key", nullable = false, length = 100)
    private String tagKey;

    @Column(name = "tag_value", nullable = false, length = 255)
    private String tagValue;

    public Metadata() {
    }

    public Metadata(Document document, String tagKey, String tagValue) {
        this.document = document;
        this.tagKey = tagKey;
        this.tagValue = tagValue;
    }

    public Integer getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(Integer metadataId) {
        this.metadataId = metadataId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        if (metadataId != null && metadata.metadataId != null) {
            return Objects.equals(metadataId, metadata.metadataId);
        }
 
        boolean documentEquals = (document == null && metadata.document == null) ||
                                 (document != null && metadata.document != null &&
                                  Objects.equals(document.getDocumentId(), metadata.document.getDocumentId())); 
        return documentEquals && Objects.equals(tagKey, metadata.tagKey);
    }

    @Override
    public int hashCode() {
         if (metadataId != null) {
            return Objects.hash(metadataId);
        }
        UUID docId = (document != null) ? document.getDocumentId() : null;
        return Objects.hash(docId, tagKey);
    }


    @Override
    public String toString() {
        return "Metadata{" +
               "metadataId=" + metadataId +

               ", documentId=" + (document != null ? document.getDocumentId() : null) +
               ", tagKey='" + tagKey + '\'' +
               ", tagValue='" + tagValue + '\'' +
               '}';
    }
}
