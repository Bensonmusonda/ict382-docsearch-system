package com.firstserverapp.serverapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstserverapp.serverapp.model.Document;
import com.firstserverapp.serverapp.model.Metadata;
import com.firstserverapp.serverapp.repository.MetadataRepository;

@Service
public class MetadataService {

    private final MetadataRepository metadataRepository;
    private final DocumentService documentService;

    @Autowired
    public MetadataService(MetadataRepository metadataRepository, DocumentService documentService) {
        this.metadataRepository = metadataRepository;
        this.documentService = documentService;
    }

    public List<Metadata> getAllMetadata() {
        return metadataRepository.findAll();
    }

    public Metadata getMetadataById(int id) {
        return metadataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Metadata not found with id: " + id));
    }

    public List<Metadata> getMetadataByDocumentId(UUID documentId) {
        return metadataRepository.findByDocumentDocumentId(documentId);
    }
    public List<Metadata> getMetadataByTagKey(String tagKey){
        return metadataRepository.findByTagKey(tagKey);
    }

    @Transactional
    public Metadata createMetadata(UUID documentId, String tagKey, String tagValue) {
        Document document = documentService.getDocumentById(documentId);
        Metadata metadata = new Metadata(document, tagKey, tagValue);
        return metadataRepository.save(metadata);
    }


    @Transactional
    public Metadata updateMetadata(int id, UUID documentId, String tagKey, String tagValue) {
        Metadata metadata = getMetadataById(id);
        Document document = documentService.getDocumentById(documentId);
        metadata.setDocument(document);
        metadata.setTagKey(tagKey);
        metadata.setTagValue(tagValue);
        return metadataRepository.save(metadata);
    }

    public void deleteMetadata(int id) {
        metadataRepository.deleteById(id);
    }
}