package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Payment;

import java.util.Date;
import java.util.List;

public interface PaymentRepository {
    Integer createPayment(Payment payment);
    Integer makePayment(Payment payment);
    List<Payment> getPayments (Date dateStart, Date dateEnd);
    Payment getPaymentById (Long paymentId);
}
