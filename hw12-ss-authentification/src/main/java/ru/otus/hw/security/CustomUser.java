package ru.otus.hw.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUser extends User {
    private String name;
    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name) {
        super(username, password, authorities);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
