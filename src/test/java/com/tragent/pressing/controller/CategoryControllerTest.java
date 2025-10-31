package com.tragent.pressing.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tragent.pressing.model.Category;
import com.tragent.pressing.model.Item;
import com.tragent.pressing.service.CategoryService;
import com.tragent.pressing.service.CustomUserDetailService;
import com.tragent.pressing.service.ItemService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ItemService itemService;
   
    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @WithMockUser
    public void testGetCategories_all() throws Exception {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Test Category 1", "Description 1"));
        categories.add(new Category("Test Category 2", "Description 2"));
        when(categoryService.findAll()).thenReturn(categories);
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetCategories_byName() throws Exception {
        Category category = new Category("Test Category", "Description");
        when(categoryService.findByName("Test Category")).thenReturn(category);
        mockMvc.perform(get("/api/v1/categories").param("categoryName", "Test Category"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetCategoryById_found() throws Exception {
        Category category = new Category("Test Category", "Description");
        when(categoryService.findById(1L)).thenReturn(category);
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetCategoryById_notFound() throws Exception {
        when(categoryService.findById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testGetCategoryItems() throws Exception {
        List<Item> items = new ArrayList<>();
        when(itemService.findCategoryItems(1L)).thenReturn(items);
        mockMvc.perform(get("/api/v1/categories/1/items"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testCreateCategory() throws Exception {
        Category category = new Category("New Category", "New Description");
        when(categoryService.create(category)).thenReturn(category);
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testUpdateCategory() throws Exception {
        Category category = new Category("Updated Category", "Updated Description");
        when(categoryService.update(category)).thenReturn(category);
        mockMvc.perform(put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());
    }
}