package com.example.Usermodule.service;

import com.example.Usermodule.dao.UserDao;
import com.example.Usermodule.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public User createUser(User user) {
        return userDao.insertUser(user) ? user : null;
    }

    public boolean deleteUser(String email) {
        return userDao.deleteUser(email);
    }

    public User findUser(String email) {
        return userDao.findUser(email);
    }

    public List<String> findAllEmails() {
        return userDao.findAllEmails();
    }

    public boolean updateUserFields(String email, HashMap<String, ?> request) {
        if (userDao.findUser(email) == null) {
            log.error("Could not update user field");
            return false;
        }
        return userDao.updateUserFields(email,
                request.get("name").toString(),
                request.get("surname").toString(),
                request.get("gender").toString(),
                request.get("birthDate").toString());
    }
}