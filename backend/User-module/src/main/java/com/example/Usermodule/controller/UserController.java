package com.example.Usermodule.controller;

import com.example.Usermodule.dto.GetUserDto;
import com.example.Usermodule.dto.UpdateUserDto;
import com.example.Usermodule.entity.User;
import com.example.Usermodule.event.OrderModuleEventClient;
import com.example.Usermodule.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final OrderModuleEventClient orderModuleEventClient;
    private final UserService userService;

    private final ModelMapper mapper;

    @GetMapping("/{username}")
    public ResponseEntity<GetUserDto> getUser(@PathVariable("username") String username) {
        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.map(user, GetUserDto.class));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetUserDto>> getUsers() {
        List<GetUserDto> commentsDto =
                userService
                        .findAllUsers()
                        .stream()
                        .map(comment ->
                                mapper.map(comment, GetUserDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(commentsDto);
    }

    @GetMapping(value = "/{username}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getUserImage(@PathVariable("username") String username) {
        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getImage());
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable("username") String username,
                                           @RequestBody UpdateUserDto request) {
        if (!userService.updateUserFields(username, request)) {
            return ResponseEntity.badRequest().build();
        }
        orderModuleEventClient.updateUser(username, request.getFunds());
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/{username}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@PathVariable("username") String username,
                                                @RequestBody MultipartFile image) throws IOException {
        if (!userService.updateImage(username, image.getInputStream())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
        if (!userService.deleteUser(username)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
