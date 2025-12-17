package com.mbarca.vete.repository;

import com.mbarca.vete.domain.WebOrder;

public interface WebOrderRepository {
    Long create(WebOrder order);
    void updatePreference(Long orderId, String preferenceId);
    void updatePayment(Long orderId, String paymentId, String status);
    WebOrder findById(Long id);

}
