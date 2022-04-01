package com.example.Authmodule.controller;


import com.example.Authmodule.dto.RegisterUserRequest;
import com.example.Authmodule.entity.AuthUser;
import com.example.Authmodule.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AuthService authService;

    private final ModelMapper mapper;

    @Autowired
    public AdminController(AuthService authService, ModelMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterUserRequest registerUserRequest) {
        authService.createAdminUser(mapper.map(registerUserRequest, AuthUser.class));
        return ResponseEntity.ok("Registration successful");
    }
}
