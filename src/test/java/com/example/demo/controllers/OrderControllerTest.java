package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private static OrderController orderController;
    private static UserRepository userRepository = mock(UserRepository.class);
    private static OrderRepository orderRepository = mock(OrderRepository.class);
    
    @BeforeClass
    public static void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);
    }

    @Test
    public void submitSuccess() {
        User user = TestUtils.getUser();
        user.setCart(new Cart(new ArrayList<>(), BigDecimal.valueOf(100),user));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit((user.getUsername()));
        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(user.getUsername(),userOrderResponseEntity.getBody().getUser().getUsername());
    }

    @Test
    public void submitFailure() {
        User user = TestUtils.getUser();
        user.setCart(new Cart(new ArrayList<>(), BigDecimal.valueOf(100),user));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit((user.getUsername()));
        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(404,userOrderResponseEntity.getStatusCodeValue());
        Assert.assertNull(userOrderResponseEntity.getBody());
    }

    @Test
    public void getOrdersSuccess() {
        User user = TestUtils.getUser();
        user.setCart(new Cart(new ArrayList<>(),BigDecimal.valueOf(100),user));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(new UserOrder(user)));

        ResponseEntity<List<UserOrder>> order = orderController.getOrdersForUser(user.getUsername());
        Assert.assertNotNull(order);
        Assert.assertEquals(user.getUsername(),order.getBody().get(0).getUser().getUsername());
    }
}
