package com.example.Usermodule.controller;

import com.example.Usermodule.dto.CreateUserRequest;
import com.example.Usermodule.entity.User;
import com.example.Usermodule.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    @Autowired
    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        User user = userService.findUser(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getUserEmails() {
        List<String> emails = userService.findAllEmails();
        return ResponseEntity.ok(emails);
    }

    @PostMapping("")
    public ResponseEntity<Void> addUser(@RequestBody CreateUserRequest request) {
        if (!userService.createUser(mapper.map(request, User.class))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable("email") String email,
                                           @RequestBody HashMap<String, String> request) {
        if (!userService.updateUserFields(email, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {
        if (!userService.deleteUser(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}