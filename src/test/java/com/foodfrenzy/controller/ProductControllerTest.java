package com.foodfrenzy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.FoodFrenzyApplication;
import com.example.demo.controllers.ProductController;
import com.example.demo.entities.Product;
import com.example.demo.services.ProductServices;

@WebMvcTest(
    controllers = ProductController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ContextConfiguration(classes = FoodFrenzyApplication.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServices productService;

    @Test
    void testAddProduct() throws Exception {
        mockMvc.perform(post("/addingProduct")
                        .param("pname", "Burger")
                        .param("pprice", "5.99")
                        .param("pdescription", "Tasty Burger"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        mockMvc.perform(get("/updatingProduct/1")
                        .param("pname", "Pizza")
                        .param("pprice", "8.99")
                        .param("pdescription", "Cheese Pizza"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));

        verify(productService, times(1)).updateproduct(any(Product.class), eq(1));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/deleteProduct/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));

        verify(productService, times(1)).deleteProduct(1);
    }
}
