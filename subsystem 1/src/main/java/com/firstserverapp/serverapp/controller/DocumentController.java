package com.firstserverapp.serverapp.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.firstserverapp.serverapp.dto.AuthorDTO;
import com.firstserverapp.serverapp.dto.DocumentDTO;
import com.firstserverapp.serverapp.model.Author;
import com.firstserverapp.serverapp.model.Document;
import com.firstserverapp.serverapp.projection.DocumentSnippet;
import com.firstserverapp.serverapp.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    private DocumentDTO convertToDTO(Document document) {
        Author author = document.getAuthor();
        AuthorDTO authorDTO = new AuthorDTO(
                author.getAuthorId(),
                author.getName(),
                author.getAffiliation(),
                author.getEmail()
        );

        return new DocumentDTO(
                document.getDocumentId(),
                document.getTitle(),
                document.getUploadDate(),
                document.getFilePath(),
                document.getContent(),
                authorDTO
        );
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        List<DocumentDTO> documentDTOs = documents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable("id") UUID id) {
        Document document = documentService.getDocumentById(id);
        DocumentDTO documentDTO = convertToDTO(document);
        return ResponseEntity.ok(documentDTO);
    }

    @GetMapping("/allcontent")
    public ResponseEntity<List<DocumentSnippet>> getDocumentContent() {
        return new ResponseEntity<>(documentService.getDocumentContent(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(
            @RequestParam("title") String title,
            @RequestParam("authorId") UUID authorId,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam("file") MultipartFile file) {
        Document savedDocument = documentService.storeDocument(title, authorId, userId, file);
        Document fetchedDocument = documentService.getDocumentById(savedDocument.getDocumentId());
        DocumentDTO documentDTO = convertToDTO(fetchedDocument);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fetchedDocument.getDocumentId())
                .toUri();

        return ResponseEntity.created(location).body(documentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable UUID id,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("authorId") UUID authorId,
                                                         @RequestParam(value = "userId", required = false) UUID userId,
                                                         @RequestParam("filePath") String filePath,
                                                         @RequestParam("content") String content) {
        Document updatedDocument = documentService.updateDocument(id, title, authorId, userId, filePath, content);
        DocumentDTO documentDTO = convertToDTO(updatedDocument);
        return new ResponseEntity<>(documentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}