/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gtp.tradeapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gtp.tradeapp.domain.Status;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.service.email.EmailService;
import com.gtp.tradeapp.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

import static java.lang.String.format;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @Autowired
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // TODO:: Should be secured
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"application/json; charset=UTF-8"})
    public Status postCreateUser(@RequestBody User user) throws JsonProcessingException {
        LOGGER.debug("Creating new user={}" + user.asJsonString());

        try {
            userService.create(user);
            return new Status(1, "User added Successfully!");
        } catch (Exception e) {
            LOGGER.error(format("Unable to process addUser for [%s]", user.toString()), e);
            return new Status(0, e.toString());
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<User> getListOfUsers() {
        LOGGER.debug("Getting list of all users={}");

        return userService.getAllUsers();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public User getUserInformation(@PathVariable Long id) {
        LOGGER.debug("Getting user for user={}" + id);

        return userService.getUserById(id).get();
    }

    // TODO:: This is incomplete - User should not be deleted - perhaps deactivated
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Status getUserDelete(@AuthenticationPrincipal User user, @RequestParam String password) throws JsonProcessingException {
        LOGGER.debug("Deleting for user={}" + user.asJsonString());
        if (userService.verifyPassword(user, password)) {
            return new Status(1, "User Successfully Deleted");
        }
        return new Status(0, "Unable to delete User");
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public Status resetPassword(@RequestParam("email") String email) {
        LOGGER.debug("Resetting password for username={}" + email);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return updatePasswordAndSendTo(user.get());
        }
        return new Status(0, "Unable to reset password for " + email);
    }

    private Status updatePasswordAndSendTo(User user) {
        final String password = generateRandomPassword();
        userService.updatePassword(user, password);
        emailService.sendEmail(
                "Plutos: Reset Password Request",
                "Your new password is : " + password,
                user.getUserUsername()
        );
        return new Status(1, "Email Has Been Sent to " + user.getUserUsername().replaceFirst("@.*", "@***"));
    }

    private String generateRandomPassword() {
        final SecureRandom secureRandom = new SecureRandom();

        return new BigInteger(130, secureRandom)
                .toString(32)
                .substring(0, 10);
    }
}
