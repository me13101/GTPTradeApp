package com.gtp.tradeapp.service.user;

import com.gtp.tradeapp.entity.Role;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.RoleRepository;
import com.gtp.tradeapp.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        LOGGER.debug("Getting user={}" + id);
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByEmail(String username) {
        LOGGER.debug("Getting user by username={}" + username.replaceFirst("@.*", "@***"));
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> getAllUsers() {
        LOGGER.debug("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public User create(User newUser) {
        User user = new User();
        //TODO:: When User Expect More Information
        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setUsername(newUser.getUserUsername());
        user.setPassword(encode(newUser.getPassword()));

        //Defaulting Cash
        user.setCash(BigDecimal.valueOf(1000000));

        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.getRoleByName("ROLE_USER").get());
        }});

        LOGGER.debug("Creating user={}" + newUser.getUserUsername().replaceFirst("@.*", "@***"));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        LOGGER.debug("Current User: " + user);
        LOGGER.debug("Verify Password: " + user);
        return user.getPassword().equals(encode(password));
    }

    private String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);
    }

    public HashMap<String, String> basicUserInfo(User user) {
        return new HashMap<String, String>() {
            {
                put("username", user.getUserUsername());
                put("firstname", user.getFirstname());
                put("lastname", user.getLastname());
            }
        };
    }
}
