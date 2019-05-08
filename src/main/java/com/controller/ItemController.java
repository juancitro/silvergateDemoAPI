package com.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model.Item;
import com.service.ItemService;

@RestController
public class ItemController {

	private final ItemService itemService;
	
	public ItemController(ItemService itemService) {
		this.itemService=itemService;
	}
	
	@GetMapping("/items")
    public List<Item> allItems(){
		return itemService.getAll();
    }
	
	@PostMapping("/items")
	public Item addItem(@RequestBody Item item) {
		return itemService.addItem(item);
	}
	
	@GetMapping("/items/{id}")
	public Item getItemById(@PathVariable Long id) {
		return itemService.getItemById(id);
	}
	
	@GetMapping("/items/search")
	public List<Item> getItemsByName(@RequestParam(value="name") String name) {
		return itemService.getItemByName(name);
	}
	
	@PutMapping("/items/{id}")
	public Item replaceItem(@RequestBody Item newItem, @PathVariable Long id) {
		return itemService.replaceItem(newItem, id);
	}

	@DeleteMapping("/items/{id}")
	void deleteEmployee(@PathVariable Long id) {
		itemService.deleteById(id);
	}

}
