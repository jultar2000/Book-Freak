package com.example.Authmodule.service;

import com.example.Authmodule.dao.AuthDao;
import com.example.Authmodule.entity.AuthUser;
import com.example.Authmodule.exceptions.IncorrectParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class AuthService {

    private final AuthDao userDao;
    private final Logger log;

    @Autowired
    public AuthService(AuthDao userDao) {
        this.userDao = userDao;
        log = LoggerFactory.getLogger(this.getClass());
    }

    public boolean createUser(AuthUser user) {
        return userDao.insertUser(user);
    }




}