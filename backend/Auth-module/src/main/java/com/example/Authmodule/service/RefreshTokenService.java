package com.example.Authmodule.service;

import com.example.Authmodule.dao.RefreshTokenDao;
import com.example.Authmodule.entity.RefreshToken;
import com.example.Authmodule.exceptions.InvalidTokenException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreationDate(Instant.now());
        refreshTokenDao.insertToken(refreshToken);
        return refreshToken;
    }

    public void validateRefreshToken(String token) {
        refreshTokenDao.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(
                                MessageFormat.format("Refresh Token '{}' is invalid.", token)));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenDao.deleteByToken(token);
    }
}
