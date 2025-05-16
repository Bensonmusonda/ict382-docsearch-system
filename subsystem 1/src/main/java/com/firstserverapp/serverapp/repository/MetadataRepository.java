package com.firstserverapp.serverapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstserverapp.serverapp.model.Metadata;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, Integer> {
    List<Metadata> findByDocumentDocumentId(UUID documentId);
    List<Metadata> findByTagKey(String tagKey);
}