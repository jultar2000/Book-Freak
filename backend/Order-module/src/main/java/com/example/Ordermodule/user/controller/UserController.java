package com.example.Ordermodule.user.controller;

import com.example.Ordermodule.user.entity.User;
import com.example.Ordermodule.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-module/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper mapper;

    @PostMapping("")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        if (!userService.insertUser(mapper.map(user, User.class))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        if (!userService.deleteUser(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
