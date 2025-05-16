package com.firstserverapp.serverapp.dto;

import java.util.UUID;

public class AuthorDTO {

    private UUID authorId;
    private String name;
    private String affiliation;
    private String email;

    public AuthorDTO(UUID authorId, String name, String affiliation, String email) {
        this.authorId = authorId;
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
    }

    public UUID getAuthorId() { return authorId; }
    public String getName() { return name; }
    public String getAffiliation() { return affiliation; }
    public String getEmail() { return email; }
}
