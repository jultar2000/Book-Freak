package com.example.Usermodule.service;

import com.example.Usermodule.dao.UserDao;
import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
import com.example.Usermodule.exceptions.IncorrectParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            log.error("Not valid email `{}`, cannot perform any operation.", email);
            throw new IncorrectParameterException(
                    MessageFormat.format("User email cannot be null or empty", email));
        }
    }

    public boolean deleteUser(String email) {
        validateEmail(email);
        return userDao.deleteUser(email);
    }

    public User findUser(String email) {
        validateEmail(email);
        return userDao.findUser(email);
    }

    public List<String> findAllEmails() {
        return userDao.findAllEmails();
    }

    public boolean updateUserFields(String email, HashMap<String, ?> request) {
        validateEmail(email);
        return userDao.updateUserFields(email,
                request.get("name").toString(),
                request.get("surname").toString(),
                request.get("gender").toString(),
                request.get("birthDate").toString());
    }
}