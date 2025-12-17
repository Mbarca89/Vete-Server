package com.mbarca.vete.repository;

import com.mbarca.vete.domain.WebOrderItem;

import java.util.List;

public interface WebOrderItemRepository {
    void create(WebOrderItem item);
    List<WebOrderItem> findByOrderId(Long orderId);
}
