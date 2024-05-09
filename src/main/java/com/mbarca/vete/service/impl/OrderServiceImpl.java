package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Order;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.dto.request.OrderRequestDto;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.OrderResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.repository.OrderRepository;
import com.mbarca.vete.service.OrderService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String createOrder(OrderRequestDto orderRequestDto) {
        Integer response = orderRepository.createOrder(mapDtoToOrder(orderRequestDto));
        if (response.equals(0)) {
            return "Error al crear la Compra!";
        }
        return "Compra creada correctamente!";
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        return mapOrderToDto(orderRepository.getOrderWithProductsById(orderId));
    }

    @Override
    public List<OrderResponseDto> getOrdersByDate(Date dateStart, Date dateEnd) {
        List<Order> orders = orderRepository.getOrdersByDate(dateStart,dateEnd);
        return orders.stream().map(this::mapOrderToDto).collect(Collectors.toList());
    }

    private Order mapDtoToOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setDate(new Date());
        order.setAmount(orderRequestDto.getAmount());
        order.setOrderProducts(orderRequestDto.getOrderProducts());
        return order;
    }

    private OrderResponseDto mapOrderToDto (Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(order.getDate());
        orderResponseDto.setDate(formattedDate);

        orderResponseDto.setAmount(order.getAmount());
        orderResponseDto.setOrderProducts(order.getOrderProducts());
        return orderResponseDto;
    }
}
