package com.foodfrenzy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.services.OrderServices;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServices orderService;

    private Orders order1;
    private Orders order2;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setU_id(1);
        user.setUname("John Doe");

        order1 = new Orders();
        order1.setoId(1);
        order1.setoName("Order 1");
        order1.setoPrice(10.0);
        order1.setoQuantity(2);
        order1.setUser(user);

        order2 = new Orders();
        order2.setoId(2);
        order2.setoName("Order 2");
        order2.setoPrice(15.0);
        order2.setoQuantity(1);
        order2.setUser(user);
    }

    @Test
    void testGetOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Orders> orders = orderService.getOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("Order 1", orders.get(0).getoName());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testSaveOrder() {
        orderService.saveOrder(order1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testUpdateOrder() {
        orderService.updateOrder(1, order1);
        verify(orderRepository, times(1)).save(order1);
        assertEquals(1, order1.getoId());
    }

    @Test
    void testDeleteOrder() {
        orderService.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetOrdersForUser() {
        when(orderRepository.findOrdersByUser(user)).thenReturn(Arrays.asList(order1, order2));

        List<Orders> orders = orderService.getOrdersForUser(user);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals(user, orders.get(0).getUser());
        verify(orderRepository, times(1)).findOrdersByUser(user);
    }
}
