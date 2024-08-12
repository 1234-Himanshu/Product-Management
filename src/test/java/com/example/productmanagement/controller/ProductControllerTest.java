package com.example.productmanagement.controller;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Product1", "Description1", 100.0);
        product.setId(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Product1\",\"description\":\"Description1\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"));

        verify(productService, times(1)).saveProduct(any(Product.class));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product1"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        when(productService.updateProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"UpdatedProduct\",\"description\":\"UpdatedDescription\",\"price\":200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedProduct"));

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).updateProduct(any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).deleteProduct(1L);
    }
}
