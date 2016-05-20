package com.gtp.tradeapp.service.user;

import com.gtp.tradeapp.entity.User;

import java.util.HashMap;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    Iterable<User> getAllUsers();

    User create(User user);

    User update(User user);

    boolean verifyPassword(User user, String password);

    void updatePassword(User user, String password);

    HashMap<String, String> basicUserInfo(User user);
}
