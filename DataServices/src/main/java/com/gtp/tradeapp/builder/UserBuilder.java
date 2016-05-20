package com.gtp.tradeapp.builder;


import com.gtp.tradeapp.entity.Role;
import com.gtp.tradeapp.entity.User;

import java.math.BigDecimal;
import java.util.HashSet;

public class UserBuilder {
    private User user;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder(Long id) {
        this();
        this.user.setId(id);
    }

    public UserBuilder withFirstname(String name) {
        this.user.setFirstname(name);
        return this;
    }

    public UserBuilder withLastname(String name) {
        this.user.setLastname(name);
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.user.setUsername(username);
        return this;
    }

    public UserBuilder withCash(BigDecimal cash) {
        this.user.setCash(cash);
        return this;
    }

    public UserBuilder asUser() {
        Role role = new Role();
        role.setId(1);

        this.user.setRoles(new HashSet<Role>() {{
            add(role);
        }});
        return this;
    }

    public User build() {
        return user;
    }
}
