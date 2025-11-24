package org.coiffet.tp1.tokenJWT;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceR5A05 implements UserDetailsService {

    private static final Map<String, UserDetails> users = new HashMap<>();

    static {
        users.put("admin", User.withUsername("admin")
                .password("admin123")
                .roles("ADMIN")
                .build());

        users.put("user", User.withUsername("user")
                .password("user123")
                .roles("USER")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}
