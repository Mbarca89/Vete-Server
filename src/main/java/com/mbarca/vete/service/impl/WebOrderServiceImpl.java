package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.dto.response.WebOrderResponseDto;
import com.mbarca.vete.repository.WebOrderRepository;
import com.mbarca.vete.service.WebOrderService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebOrderServiceImpl implements WebOrderService {

    private final WebOrderRepository webOrderRepository;

    public WebOrderServiceImpl(WebOrderRepository webOrderRepository) {
        this.webOrderRepository = webOrderRepository;
    }

    @Override
    public List<WebOrderResponseDto> getOrdersByDate(Date dateStart, Date dateEnd) {
        List<WebOrder> orders = webOrderRepository.findByDate(dateStart, dateEnd);
        return orders.stream().map(this::mapOrderToDto).collect(Collectors.toList());}

    @Override
    public String shipOrder(Long orderId){
        Integer response = webOrderRepository.shipOrder(orderId);
        if (response.equals(0)) {
            return "Error al actualizar la orden";
        }
        return "Orden actualizada correctamente!";
    }

    private WebOrderResponseDto mapOrderToDto(WebOrder order) {
        WebOrderResponseDto responseDto = new WebOrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setCreatedAt(order.getCreatedAt());
        responseDto.setStatus(order.getStatus());
        responseDto.setShipped(order.isShipped());
        responseDto.setCustomerName(order.getCustomerName());
        responseDto.setCustomerEmail(order.getCustomerEmail());
        responseDto.setCustomerPhone(order.getCustomerPhone());
        responseDto.setTotalAmount(order.getTotalAmount());
        return responseDto;
    }
}
