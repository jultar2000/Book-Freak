package com.example.Authmodule.service;

import com.example.Authmodule.dao.VerificationTokenDao;
import com.example.Authmodule.entity.VerificationToken;
import com.example.Authmodule.exceptions.InvalidTokenException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenDao verificationTokenDao;

    public void insertVerificationToken(VerificationToken verificationToken) {
        verificationTokenDao.insertToken(verificationToken);
    }

    public VerificationToken findByToken(String token) {
        return verificationTokenDao.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(
                        MessageFormat.format("Token '{}' is invalid", token)));
    }
}
