package com;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Item;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
    public void test_all() throws Exception {
		
		/*
		 * Items pre-cargados en la BD:
		 * 		* id=1, name=Pala, price=20
		 * 		* id=2, name=Martillo, price=10 
		 */
		
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		// addItem
		Item itemToAdd = new Item("Pinza",40);
		String jsonItemToAdd = mapper.writeValueAsString(itemToAdd);
		this.mockMvc.perform(post("/items").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(jsonItemToAdd))
					.andDo(print())
					.andExpect(status().isOk());
		
		// getItemById
		this.mockMvc.perform(get("/items/{id}",(long) 1))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.price", is(20.0)))
		            .andExpect(jsonPath("$.name", is("Pala")));
		
		// getItemByName
		this.mockMvc.perform(get("/items/search").param("name", "Martillo"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(1)))
					.andExpect(jsonPath("$[0].price", is(10.0)))
		            .andExpect(jsonPath("$[0].name", is("Martillo")));
		
		// getAll
		this.mockMvc.perform(get("/items"))
					.andDo(print())
        			.andExpect(status().isOk())
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			        .andExpect(jsonPath("$", hasSize(3)))
			        .andExpect(jsonPath("$[0].price", is(20.0)))
			        .andExpect(jsonPath("$[0].name", is("Pala")))
			        .andExpect(jsonPath("$[1].price", is(10.0)))
			        .andExpect(jsonPath("$[1].name", is("Martillo")))
					.andExpect(jsonPath("$[2].price", is(40.0)))
			        .andExpect(jsonPath("$[2].name", is("Pinza")));

		// replace
		Item itemReplace = new Item("Destornillador",30);
		String jsonItemReplace = mapper.writeValueAsString(itemReplace);
		this.mockMvc.perform(put("/items/{id}", (long) 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(jsonItemReplace))
					.andDo(print())
					.andExpect(status().isOk());
		this.mockMvc.perform(get("/items/{id}",(long) 1))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.price", is(30.0)))
				    .andExpect(jsonPath("$.name", is("Destornillador")));	
		
		// delete
		this.mockMvc.perform(delete("/items/{id}", (long) 2).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk());
		this.mockMvc.perform(get("/items"))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			        .andExpect(jsonPath("$", hasSize(2)))
				    .andExpect(jsonPath("$[0].price", is(40.0)))
				    .andExpect(jsonPath("$[0].name", is("Pinza")))
					.andExpect(jsonPath("$[1].price", is(30.0)))
				    .andExpect(jsonPath("$[1].name", is("Destornillador")));
    }

}
