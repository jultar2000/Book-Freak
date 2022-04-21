package com.example.Authmodule.controller;

import com.example.Authmodule.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class CommunicationController {

    private final JwtService jwtService;

    @Autowired
    public CommunicationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/communication")
    public ResponseEntity<String> getCurrentUsername(HttpServletRequest request) {
        String jwt = jwtService.getJwtFromRequest(request);
        String username = jwtService.extractUsernameFromToken(jwt);
        return ResponseEntity.ok(username);
    }
}