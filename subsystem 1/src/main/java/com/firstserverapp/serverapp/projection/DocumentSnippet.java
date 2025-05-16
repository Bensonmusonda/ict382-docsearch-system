package com.firstserverapp.serverapp.projection;

import java.util.UUID;

public interface DocumentSnippet {
    UUID getDocumentId();
    String getContent();
}
