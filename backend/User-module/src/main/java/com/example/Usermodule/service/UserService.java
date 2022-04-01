package com.example.Usermodule.service;

import com.example.Usermodule.dao.UserDao;
import com.example.Usermodule.dto.UpdateUserDto;
import com.example.Usermodule.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final Logger log;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
        log = LoggerFactory.getLogger(this.getClass());
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.error("Not valid username `{}`, cannot perform any operation.", username);
            throw new IllegalArgumentException("User username cannot be null or empty");
        }
    }

    public boolean deleteUser(String username) {
        validateUsername(username);
        return userDao.deleteUser(username);
    }

    public User findUser(String username) {
        validateUsername(username);
        return userDao.findUser(username);
    }

    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    public boolean updateUserFields(String username, UpdateUserDto request) {
        validateUsername(username);
        return userDao.updateUserFields(username,
                request.getName(),
                request.getSurname(),
                request.getGender(),
                request.getBirthDate());
    }
}