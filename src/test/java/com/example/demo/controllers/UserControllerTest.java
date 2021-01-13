package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"encoder",bCryptPasswordEncoder);
    }

    @Test
    public void createUserSuccess(){
        when(bCryptPasswordEncoder.encode("adminPass")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("admin");
        r.setPassword("adminPass");
        r.setConfirmPassword("adminPass");

        final ResponseEntity<User> response = userController.createUser(r);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0,u.getId());
        Assert.assertEquals("admin",u.getUsername());
        Assert.assertEquals("thisIsHashed",u.getPassword());
    }

    @Test
    public void findByUsernameNotFound() {
        ResponseEntity<User> responseEntity = userController.findByUserName("localUser");
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(404,responseEntity.getStatusCodeValue());
    }

    @Test
    public void findByUsernameSuccess() {
        String username = "testUser";
        String password = "Password";

        User user = new User(username,password,null);
        when(userRepository.findByUsername(username)).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName(username);
        Assert.assertNotNull(userResponseEntity);
        Assert.assertEquals(200,userResponseEntity.getStatusCodeValue());

        User u = userResponseEntity.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(username,u.getUsername());
    }
}
