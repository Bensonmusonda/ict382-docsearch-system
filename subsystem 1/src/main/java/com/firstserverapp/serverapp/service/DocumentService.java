package com.firstserverapp.serverapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.firstserverapp.serverapp.config.FileStorageProperties;
import com.firstserverapp.serverapp.model.Author;
import com.firstserverapp.serverapp.model.Document;
import com.firstserverapp.serverapp.model.User;
import com.firstserverapp.serverapp.projection.DocumentSnippet;
import com.firstserverapp.serverapp.repository.DocumentRepository;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorService authorService;
    private final UserService userService;
    private final Path fileStorageLocation;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, AuthorService authorService, UserService userService,
                           FileStorageProperties fileStorageProperties) {
        this.documentRepository = documentRepository;
        this.authorService = authorService;
        this.userService = userService;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public Document storeDocument(String title, UUID authorId, UUID userId, MultipartFile file) {
        Author author = authorService.getAuthorById(authorId);
        User user = null;
        if (userId != null) {
            user = userService.getUserById(userId);
        }

        String fileName = storeFile(file);
        String filePath = this.fileStorageLocation.resolve(fileName).toString();
        String content = extractTextFromFile(file);

        Document document = new Document(title, author, user, filePath, content);
        return documentRepository.save(document);
    }

    private String extractTextFromFile(MultipartFile file) {
        String text = "";
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            try (InputStream inputStream = file.getInputStream()) {
                if (fileExtension.equals("pdf")) {
                    byte[] bytes = inputStream.readAllBytes(); 
                    PDDocument document = Loader.loadPDF(bytes);
                    PDFTextStripper stripper = new PDFTextStripper();
                    text = stripper.getText(document);
                    document.close();
                } else if (fileExtension.equals("docx")) {
                    XWPFDocument document = new XWPFDocument(inputStream);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                    text = extractor.getText();
                    extractor.close();
                    document.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not extract text from " + fileName, e);
            }
        }
        return text;
    }

    private String storeFile(MultipartFile file) {
        String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found with id: " + id));
    }

    public List<DocumentSnippet> getDocumentContent() {
        return documentRepository.findAllBy();
    }

    @Transactional
    public Document updateDocument(UUID id, String title, UUID authorId, UUID userId, String filePath, String content) {
        Document document = getDocumentById(id);
        document.setTitle(title);
        Author author = authorService.getAuthorById(authorId);
        document.setAuthor(author);
        User user = null;
        if (userId != null) {
            user = userService.getUserById(userId);
        }
        document.setUser(user);
        document.setFilePath(filePath);
        document.setContent(content);
        document.setUploadDate(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public void deleteDocument(UUID id) {
        documentRepository.deleteById(id);
    }
}