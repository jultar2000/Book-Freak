package com.example.Authmodule.service;

import com.example.Authmodule.dao.AuthDao;
import com.example.Authmodule.entity.AuthUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final AuthDao authDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AuthUser user = authDao.findUserByUsername(username);
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(),
                        user.isEnabled(), true, true,
                        true, user.getRole().getGrantedAuthorities());
    }
}
