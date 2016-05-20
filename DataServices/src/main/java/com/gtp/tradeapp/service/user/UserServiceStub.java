package com.gtp.tradeapp.service.user;

import com.gtp.tradeapp.builder.UserBuilder;
import com.gtp.tradeapp.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class UserServiceStub implements UserService {
    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(getSampleUser());
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.of(getSampleUser());
    }

    @Override
    public Iterable<User> getAllUsers() {
        return Collections.singletonList(getSampleUser());
    }

    @Override
    public User create(User user) {
        return getSampleUser();
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void updatePassword(User user, String password) {
        //do nothing
    }

    @Override
    public HashMap<String, String> basicUserInfo(User user) {
        return null;
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return false;
    }

    private User getSampleUser() {
        return new UserBuilder()
                .withUsername("limluc")
                .withFirstname("Lucky")
                .withLastname("Lim")
                .withPassword("123456")
                .asUser()
                .build();
    }
}
