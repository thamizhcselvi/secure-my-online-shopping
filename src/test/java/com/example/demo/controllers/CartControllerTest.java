package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private static CartController cartController;
    private static UserRepository userRepository = mock(UserRepository.class);
    private static CartRepository cartRepository = mock(CartRepository.class);
    private static ItemRepository itemRepository = mock(ItemRepository.class);
    
    @BeforeClass
    public static void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);
    }

    @Test
    public void addToCart_UserErrorFailure() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("",1,1);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(cartResponseEntity);
        Assert.assertEquals(404,cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartSuccess() {
        String username = "admin";
        String password = "adminPass";
        User user = new User(username,password,null);
        user.setCart(new Cart(new ArrayList<>(), BigDecimal.valueOf(100),user));

        Item item = new Item("item",BigDecimal.valueOf(10));
        ModifyCartRequest request = new ModifyCartRequest(username,1,1);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(user,responseEntity.getBody().getUser());
    }

    @Test
    public void removeFromCartSuccess() {
        String username = "admin";
        String password = "adminPass";
        User user = new User(username,password,null);
        user.setCart(new Cart(new ArrayList<>(), BigDecimal.valueOf(100),user));

        Item item = new Item("item",BigDecimal.valueOf(10));
        ModifyCartRequest request = new ModifyCartRequest(username,1,1);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(user,responseEntity.getBody().getUser());
    }
}
