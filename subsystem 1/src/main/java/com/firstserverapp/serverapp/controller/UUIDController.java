package com.firstserverapp.serverapp.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uuid")
public class UUIDController {

    @GetMapping("/generate")
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}