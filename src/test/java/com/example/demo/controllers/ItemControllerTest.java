package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private static ItemController itemController;
    private static ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeClass
    public static void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
    }

    @Test
    public void getItemsSuccess() {
        String item = "item";
        when(itemRepository.findAll()).thenReturn(Arrays.asList(new Item(item, BigDecimal.valueOf(10))));

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItems();
        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(item,listResponseEntity.getBody().get(0).getName());

    }

    @Test
    public void getItemsByNameSuccess() {
        String name = "item";
        Item item = new Item(name,BigDecimal.valueOf(10));

        when(itemRepository.findByName(name)).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName(name);
        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(name,listResponseEntity.getBody().get(0).getName());
    }

    @Test
    public void getItemsByNameFailure() {
        String name = "item";
        Item item = new Item(name,BigDecimal.valueOf(10));

        when(itemRepository.findByName(name)).thenReturn(null);

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName(name);
        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(404,listResponseEntity.getStatusCodeValue());
    }
}
