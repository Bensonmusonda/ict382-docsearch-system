package com.firstserverapp.serverapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstserverapp.serverapp.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
     Optional<Tag> findByName(String name);
}