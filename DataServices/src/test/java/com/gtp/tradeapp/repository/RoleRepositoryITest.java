package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class RoleRepositoryITest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void shouldReturnRole() {
        assertNotNull(roleRepository.getRoleByName("ROLE_USER"));
        assertNotNull(roleRepository.getRoleByName("ROLE_GUEST"));
        assertNotNull(roleRepository.getRoleByName("ROLE_ADMIN"));
    }
}