package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.exception.ItemNotFoundException;
import com.model.Item;
import com.repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
    private ItemRepository repository;
	
	public List<Item> getAll(){
		return repository.findAll();
	}

	public Item addItem(Item item) {
		return repository.save(item);
	}

	public Item getItemById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
	}

	public List<Item> getItemByName(String name) {
		return repository.findByName(name);
	}
	
	public Item replaceItem(Item newItem, Long id) {
		return repository.findById(id)
			.map(item -> {
				item.setName(newItem.getName());
				item.setPrice(newItem.getPrice());
				return repository.save(item);
			})
			.orElseGet(() -> {
				return repository.save(newItem);
			});
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
