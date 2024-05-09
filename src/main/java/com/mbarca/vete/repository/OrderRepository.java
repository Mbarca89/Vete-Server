package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository {
    Integer createOrder (Order order);
    Order getOrderWithProductsById(long orderId);
    List<Order> getOrdersByDate(Date dateStart, Date dateEnd);
}
