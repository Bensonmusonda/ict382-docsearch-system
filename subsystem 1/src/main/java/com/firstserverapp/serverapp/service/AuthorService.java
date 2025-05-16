package com.firstserverapp.serverapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstserverapp.serverapp.model.Author;
import com.firstserverapp.serverapp.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
    }
     public Author getAuthorByName(String name) {
        return authorRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Author not found with name: " + name));
    }

    @Transactional
    public Author createAuthor(String name, String affiliation, String email) {
        Author author = new Author(name, affiliation, email);
        return authorRepository.save(author);
    }

    @Transactional
    public Author updateAuthor(UUID id, String name, String affiliation, String email) {
        Author author = getAuthorById(id);
        author.setName(name);
        author.setAffiliation(affiliation);
        author.setEmail(email);
        return authorRepository.save(author);
    }

    public void deleteAuthor(UUID id) {
        authorRepository.deleteById(id);
    }
}