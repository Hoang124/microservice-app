package com.example.order.service;

import com.example.order.dto.OrderRequest;
import com.example.order.client.InventoryClient;
import com.example.order.event.OrderPlacedEvent;
import com.example.order.model.Order;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        boolean isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (!isInStock) {
            throw new IllegalArgumentException("Product is not in stock");
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setSkuCode(orderRequest.skuCode());
        order.setPrice(orderRequest.price() != null ? orderRequest.price() : BigDecimal.ZERO);
        order.setQuantity(orderRequest.quantity() != null ? orderRequest.quantity() : 1);
        orderRepository.save(order);

        //Send the message to Kafka Topic
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderNumber(order.getOrderNumber());
        orderPlacedEvent.setEmail(orderRequest.userDetails().email());
        orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
        orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
        log.info("Start - Sending OrderPlacedEvent to Kafka: {}", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
    }
}
