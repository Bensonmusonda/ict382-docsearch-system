package com.firstserverapp.serverapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstserverapp.serverapp.model.Document;
import com.firstserverapp.serverapp.projection.DocumentSnippet;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<DocumentSnippet> findAllBy();
}
