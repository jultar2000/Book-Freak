package com.example.Authmodule.controller;

import com.example.Authmodule.dto.AuthUserResponse;
import com.example.Authmodule.dto.LoginUserRequest;
import com.example.Authmodule.dto.RefreshTokenRequest;
import com.example.Authmodule.dto.RegisterUserRequest;
import com.example.Authmodule.entity.AuthUser;
import com.example.Authmodule.service.AuthService;
import com.example.Authmodule.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    private final ModelMapper mapper;

    @Autowired
    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          ModelMapper mapper) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.mapper = mapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterUserRequest registerUserRequest) {
        authService.signup(mapper.map(registerUserRequest, AuthUser.class));
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok("Account Verified");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("refresh/token")
    public ResponseEntity<AuthUserResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok("Logout successful.");
    }
}
