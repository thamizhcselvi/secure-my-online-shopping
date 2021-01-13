package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("All the registered items are listed.");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> optionalItem = itemRepository.findById(id);
		if(optionalItem.isPresent()){
			log.info("Item found.Item Id:"+String.valueOf(id));
			return ResponseEntity.of(itemRepository.findById(id));
		}
		else{
			log.error("Item not found.Incorrect/Unregistered item id:"+String.valueOf(id));
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if(items==null || items.isEmpty()){
			log.error("Item not found.Incorrect/Unregistered item name:"+name);
			return ResponseEntity.notFound().build();
		}
		else {
			log.info("Item found.Item name:" + name);
			return ResponseEntity.ok(items);
		}
			
	}
	
}
