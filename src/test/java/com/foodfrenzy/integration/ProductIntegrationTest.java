package com.foodfrenzy.integration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.FoodFrenzyApplication;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

@SpringBootTest(classes = FoodFrenzyApplication.class, properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
        "spring.jpa.hibernate.ddl-auto=none"
})
@Import(ProductIntegrationTest.TestDbConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @TestConfiguration
    public static class TestDbConfig {
        @Bean
        public DataSource dataSource() throws Exception {
            DataSource mockDataSource = mock(DataSource.class);
            java.sql.Connection mockConnection = mock(java.sql.Connection.class);
            java.sql.DatabaseMetaData mockMetaData = mock(java.sql.DatabaseMetaData.class);
            java.sql.Statement mockStatement = mock(java.sql.Statement.class);
            java.sql.ResultSet mockResultSet = mock(java.sql.ResultSet.class);

            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.getMetaData()).thenReturn(mockMetaData);
            when(mockMetaData.getConnection()).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            when(mockMetaData.getDatabaseProductName()).thenReturn("MySQL");
            when(mockMetaData.getDatabaseProductVersion()).thenReturn("8.0");
            when(mockMetaData.getDriverName()).thenReturn("MySQL Connector/J");
            when(mockMetaData.getDriverVersion()).thenReturn("8.0");

            return mockDataSource;
        }
    }

    @Test
    void testAddAndGetProductFlow() throws Exception {
        mockMvc.perform(post("/addingProduct")
                .param("pname", "Super Burger")
                .param("pprice", "12.99")
                .param("pdescription", "Super delicious burger"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));

        verify(productRepository, times(1)).save(any(Product.class));

        Product product = new Product();
        product.setPid(1);
        product.setPname("Super Burger");
        product.setPprice(12.99);
        product.setPdescription("Super delicious burger");
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // 3. Delete product
        mockMvc.perform(get("/deleteProduct/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));

        verify(productRepository, times(1)).deleteById(1);
    }
}
