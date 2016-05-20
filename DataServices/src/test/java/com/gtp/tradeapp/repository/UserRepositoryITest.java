package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.builder.UserBuilder;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.util.IteratorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class UserRepositoryITest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldAddUser() {
        User user = new UserBuilder()
                .withUsername("limluc")
                .withFirstname("Lucky")
                .withLastname("Lim")
                .withPassword("123456")
                .withCash(new BigDecimal(100000))
                .asUser()
                .build();

        userRepository.save(user);

        assertEquals(10, IteratorUtil.getSize(userRepository.findAll()));
        assertNotNull(userRepository.findByUsername("limluc").get());
    }


    @Test
    public void shouldDeleteUser() {
        userRepository.delete(userRepository.findByUsername("limluc").get());

        assertEquals(9, IteratorUtil.getSize(userRepository.findAll()));
        assertEquals(false, userRepository.findByUsername("limluc").isPresent());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailAddingUserWhenEmptyCash() {
        User user = new UserBuilder()
                .withUsername("limluc")
                .withFirstname("Lucky")
                .withLastname("Lim")
                .withPassword("123456")
                .asUser()
                .build();

        userRepository.save(user);
        fail("Should Fail If Cash is Empty");
    }
}