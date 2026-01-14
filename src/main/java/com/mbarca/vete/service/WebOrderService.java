package com.mbarca.vete.service;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.dto.response.WebOrderResponseDto;
import com.mbarca.vete.dto.response.WebOrderResponseWithItemsDto;

import java.util.Date;
import java.util.List;

public interface WebOrderService {
    public List<WebOrderResponseDto> getOrdersByDate(Date dateStart, Date dateEnd);

    public WebOrderResponseWithItemsDto getOrderById(Long orderId);

    public String shipOrder(Long orderId);
}