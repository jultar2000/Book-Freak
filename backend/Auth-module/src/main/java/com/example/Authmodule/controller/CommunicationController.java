package com.example.Authmodule.controller;

import com.example.Authmodule.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CommunicationController {

    private final AuthService authService;

    @Value("${app.security.communicationKey}")
    private String communicationKey;

    @Autowired
    public CommunicationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/communication/{key}")
    public ResponseEntity<String> getCurrentUsername(@PathVariable("key") String key) {
        if(!key.equals(communicationKey))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authService.getCurrentUsername());
    }
}
