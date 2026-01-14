package com.mbarca.vete.repository;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderWithItems;

import java.util.Date;
import java.util.List;

public interface WebOrderRepository {
    Long create(WebOrder order);
    void updatePreference(Long orderId, String preferenceId);
    void updatePayment(Long orderId, String paymentId, String status);
    WebOrder findById(Long id);
    List<WebOrder> findPendingOlderThanMinutes(int minutes);
    List<WebOrder> findByDate(Date dateStart, Date dateEnd);
    int updateStatusAndPaymentIfPending(Long orderId, String status, String paymentId);
    int updateStatusIfPending(Long orderId, String status);
    int updatePaymentIdIfNull(Long orderId, String paymentId);
    WebOrderWithItems findOrderById(Long orderId);
    int shipOrder(Long orderId);

}
