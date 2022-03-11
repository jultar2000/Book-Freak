package com.example.Usermodule.service;

import com.example.Usermodule.dao.UserDao;
import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDao userDao;
    private Logger log;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
        log = LoggerFactory.getLogger(this.getClass());
    }

    public User createUser(User user) {
        if (user == null) {
            log.error("User is null");
            throw new NullArgumentException("Cannot create null user");
        } else {
            return userDao.insertUser(user) ? user : null;
        }
    }

    public boolean deleteUser(String email) {
        if (userDao.findUser(email) == null) {
            log.error("User is null");
            throw new NullArgumentException("Cannot create null user");
        } else {
            return userDao.deleteUser(email);
        }
    }
}
