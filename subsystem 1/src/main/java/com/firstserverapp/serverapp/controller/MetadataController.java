package com.firstserverapp.serverapp.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.firstserverapp.serverapp.model.Metadata;
import com.firstserverapp.serverapp.service.MetadataService;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    @Autowired
    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping
    public ResponseEntity<List<Metadata>> getAllMetadata() {
        List<Metadata> metadata = metadataService.getAllMetadata();
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metadata> getMetadataById(@PathVariable int id) {
        Metadata metadata = metadataService.getMetadataById(id);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<Metadata>> getMetadataByDocumentId(@PathVariable UUID documentId) {
        List<Metadata> metadata = metadataService.getMetadataByDocumentId(documentId);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }
     @GetMapping("/tagKey/{tagKey}")
    public ResponseEntity<List<Metadata>> getMetadataByTagKey(@PathVariable String tagKey) {
        List<Metadata> metadata = metadataService.getMetadataByTagKey(tagKey);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Metadata> createMetadata(
            @RequestParam("documentId") UUID documentId,
            @RequestParam("tagKey") String tagKey,
            @RequestParam("tagValue") String tagValue) {
        Metadata savedMetadata = metadataService.createMetadata(documentId, tagKey, tagValue);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMetadata.getMetadataId())
                .toUri();
        return ResponseEntity.created(location).body(savedMetadata);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metadata> updateMetadata(
            @PathVariable int id,
            @RequestParam("documentId") UUID documentId,
            @RequestParam("tagKey") String tagKey,
            @RequestParam("tagValue") String tagValue) {
        Metadata updatedMetadata = metadataService.updateMetadata(id, documentId, tagKey, tagValue);
        return new ResponseEntity<>(updatedMetadata, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetadata(@PathVariable int id) {
        metadataService.deleteMetadata(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}