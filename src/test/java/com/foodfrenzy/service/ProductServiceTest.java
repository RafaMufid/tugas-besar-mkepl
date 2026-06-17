package com.foodfrenzy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.ProductServices;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServices productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setPid(1);
        product1.setPname("Burger");
        product1.setPprice(5.99);
        product1.setPdescription("Delicious beef burger");

        product2 = new Product();
        product2.setPid(2);
        product2.setPname("Pizza");
        product2.setPprice(8.99);
        product2.setPdescription("Cheese pizza");
    }

    @Test
    void testAddProduct() {
        productService.addProduct(product1);
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Burger", products.get(0).getPname());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        Product found = productService.getProduct(1);

        assertNotNull(found);
        assertEquals(1, found.getPid());
        assertEquals("Burger", found.getPname());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        Product updatedProduct = new Product();
        updatedProduct.setPname("Updated Burger");
        updatedProduct.setPprice(6.99);
        updatedProduct.setPdescription("Updated Delicious beef burger");

        productService.updateproduct(updatedProduct, 1);

        verify(productRepository, times(1)).save(updatedProduct);
        assertEquals(1, updatedProduct.getPid());
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetProductByName() {
        when(productRepository.findByPname("Burger")).thenReturn(product1);

        Product found = productService.getProductByName("Burger");

        assertNotNull(found);
        assertEquals("Burger", found.getPname());
        verify(productRepository, times(1)).findByPname("Burger");
    }

    @Test
    void testGetProductByName_NotFound() {
        when(productRepository.findByPname("Sushi")).thenReturn(null);

        Product found = productService.getProductByName("Sushi");

        assertNull(found);
        verify(productRepository, times(1)).findByPname("Sushi");
    }
}
