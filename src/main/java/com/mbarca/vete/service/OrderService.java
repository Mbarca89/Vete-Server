package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.OrderRequestDto;
import com.mbarca.vete.dto.response.OrderResponseDto;

import java.util.Date;
import java.util.List;

public interface OrderService {
    String createOrder (OrderRequestDto orderRequestDto);
    OrderResponseDto getOrderById (Long orderId);
    List<OrderResponseDto> getOrdersByDate (Date dateStart, Date dateEnd);
}
