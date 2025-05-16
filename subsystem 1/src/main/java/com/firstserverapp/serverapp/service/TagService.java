package com.firstserverapp.serverapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstserverapp.serverapp.model.Tag;
import com.firstserverapp.serverapp.repository.TagRepository;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));
    }
    public Tag getTagByName(String name) {
         return tagRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Tag not found with name: " + name));
    }

    @Transactional
    public Tag createTag(String name) {
        Tag tag = new Tag(name);
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(UUID id, String name) {
        Tag tag = getTagById(id);
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public void deleteTag(UUID id) {
        tagRepository.deleteById(id);
    }
}