package com.mbarca.vete.service;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.dto.response.WebOrderResponseDto;

import java.util.Date;
import java.util.List;

public interface WebOrderService {
    public List<WebOrderResponseDto> getOrdersByDate(Date dateStart, Date dateEnd);
    public String shipOrder(Long orderId);
}
