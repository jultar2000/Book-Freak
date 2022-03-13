package com.example.Usermodule.controller;

import com.example.Usermodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("{email}")
    public ResponseEntity<Void> updateUser(@PathVariable("email") String email,
                                           @RequestBody HashMap<String, String> request) {
        if(!userService.updateUserFields(email, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }
}
