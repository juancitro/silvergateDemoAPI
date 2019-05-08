package com;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.controller.ItemController;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Item;
import com.service.ItemService;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerUnitTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private ItemService itemService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void test_allItems() throws Exception {
	    List<Item> items = Arrays.asList(
	            new Item("Martillo", 20),
	            new Item("Pala", 10));
	    when(itemService.getAll()).thenReturn(items);
	    this.mockMvc.perform(get("/items"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
	            .andExpect(jsonPath("$", hasSize(2)))
	            .andExpect(jsonPath("$[0].price", is(20.0)))
	            .andExpect(jsonPath("$[0].name", is("Martillo")))
	            .andExpect(jsonPath("$[1].price", is(10.0)))
	            .andExpect(jsonPath("$[1].name", is("Pala")));
	    verify(itemService, times(1)).getAll();
	}
	
	
	@Test
    public void test_addItem() throws Exception {
		mapper.setSerializationInclusion(Include.NON_NULL);
		Item item = new Item("Destonillador",5);
		item.setId((long) 1);
		when(itemService.addItem(item)).thenReturn(item);
		item.setId(null);
		String jsonItem = mapper.writeValueAsString(item);
		this.mockMvc.perform(post("/items").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
											.content(jsonItem))
					.andDo(print())
					.andExpect(status().isOk());
    }
	
	
	@Test
    public void test_getItemById() throws Exception {
		mapper.setSerializationInclusion(Include.NON_NULL);
		Item item = new Item("Destonillador",5);
		item.setId((long) 1);
		when(itemService.getItemById((long)1)).thenReturn(item);
		this.mockMvc.perform(get("/items/{id}",(long) 1))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.price", is(5.0)))
		            .andExpect(jsonPath("$.name", is("Destonillador")));
    }
	
	@Test
    public void test_getItemByName() throws Exception {
		List<Item> itemsMartillo = Arrays.asList(new Item("Martillo", 20));
		when(itemService.getItemByName("Martillo")).thenReturn(itemsMartillo);
		this.mockMvc.perform(get("/items/search").param("name", "Martillo"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(1)))
					.andExpect(jsonPath("$[0].price", is(20.0)))
			        .andExpect(jsonPath("$[0].name", is("Martillo")));
    }

}
