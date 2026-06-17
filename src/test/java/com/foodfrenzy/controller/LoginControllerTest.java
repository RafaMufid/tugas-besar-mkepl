package com.foodfrenzy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.FoodFrenzyApplication;
import com.example.demo.controllers.AdminController;
import com.example.demo.controllers.HomeController;
import com.example.demo.entities.User;
import com.example.demo.services.AdminServices;
import com.example.demo.services.OrderServices;
import com.example.demo.services.ProductServices;
import com.example.demo.services.UserServices;

@WebMvcTest(
    controllers = {AdminController.class, HomeController.class},
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ContextConfiguration(classes = FoodFrenzyApplication.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userService;

    @MockBean
    private AdminServices adminService;

    @MockBean
    private ProductServices productService;

    @MockBean
    private OrderServices orderService;

    @Test
    void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(model().attributeExists("adminLogin"));
    }

    @Test
    void testAdminLogin_Success() throws Exception {
        when(adminService.validateAdminCredentials("admin@example.com", "admin123")).thenReturn(true);

        mockMvc.perform(post("/adminLogin")
                        .param("email", "admin@example.com")
                        .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
    }

    @Test
    void testAdminLogin_Failure() throws Exception {
        when(adminService.validateAdminCredentials("admin@example.com", "wrong")).thenReturn(false);

        mockMvc.perform(post("/adminLogin")
                        .param("email", "admin@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid email or password"));
    }

    @Test
    void testUserLogin_Success() throws Exception {
        User user = new User();
        user.setU_id(1);
        user.setUname("John Doe");
        user.setUemail("john@example.com");

        when(userService.validateLoginCredentials("john@example.com", "user123")).thenReturn(true);
        when(userService.getUserByEmail("john@example.com")).thenReturn(user);
        when(orderService.getOrdersForUser(user)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/userLogin")
                        .param("userEmail", "john@example.com")
                        .param("userPassword", "user123"))
                .andExpect(status().isOk())
                .andExpect(view().name("BuyProduct"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("name", "John Doe"));
    }

    @Test
    void testUserLogin_Failure() throws Exception {
        when(userService.validateLoginCredentials("john@example.com", "wrong")).thenReturn(false);

        mockMvc.perform(post("/userLogin")
                        .param("userEmail", "john@example.com")
                        .param("userPassword", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(model().attributeExists("error2"))
                .andExpect(model().attribute("error2", "Invalid email or password"));
    }
}
