package com.example.kafka.controller;

import com.example.kafka.model.Order;
import com.example.kafka.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final AtomicInteger orderIdCounter = new AtomicInteger();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void createOrder(@RequestBody Order order) {
        log.info("Create order called: order={}", order);
        int orderId = orderIdCounter.incrementAndGet();
        var productName = order.product() +
                ThreadLocalRandom.current().nextInt(100);
        var orderToSave = new Order(
                Integer.toString(orderId),
                productName,
                order.price(),
                order.quantity()
        );
        orderService.saveOrder(orderToSave);
    }
}