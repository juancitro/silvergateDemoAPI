package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Item;

public interface ItemRepository  extends JpaRepository<Item, Long>{
	
	public List<Item> findByName(String name);

}
