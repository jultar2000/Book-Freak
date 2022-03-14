package com.example.Usermodule.service;

import com.example.Usermodule.dao.UserDao;
import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
import com.example.Usermodule.exceptions.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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
        try {
            return userDao.insertUser(user) ? user : null;
        } catch (IncorrectDaoOperation e) {
            log.error("Could not create user");
        }
        return null;
    }

    public boolean deleteUser(String email) {
        if (userDao.findUser(email) == null) {
            log.error("Could not delete user");
            throw new NullArgumentException("Cannot create null user");
        } else {
            return userDao.deleteUser(email);
        }
    }

    public User findUser(String email) {
        if (email == null) {
            log.error("Could not find user");
            throw new NullArgumentException("Cannot find null user");
        } else {
            return userDao.findUser(email);
        }
    }

    public List<String> findAllEmails() {
       return userDao.findAllEmails();
    }

    public boolean updateUserFields(String email, HashMap<String, ?> request) {
        if (userDao.findUser(email) == null) {
            log.error("Could not update user field");
            throw new NullArgumentException("Cannot create null user");
        } else {
            return
                    userDao.updateUserField(email, "name", request.get("name").toString()) &
                    userDao.updateUserField(email, "surname", request.get("surname").toString()) &
                    userDao.updateUserField(email, "gender", request.get("gender").toString()) &
                    userDao.updateUserField(email, "birthDate", request.get("birthDate").toString());
        }
    }
}