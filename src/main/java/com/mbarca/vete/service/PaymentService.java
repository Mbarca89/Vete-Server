package com.mbarca.vete.service;

import com.mbarca.vete.domain.Payment;
import com.mbarca.vete.dto.request.PaymentRequestDto;
import com.mbarca.vete.dto.response.PaymentResponseDto;

import java.util.Date;
import java.util.List;

public interface PaymentService {
    String createPayment(PaymentRequestDto paymentRequestDto);
    String makePayment(PaymentRequestDto paymentRequestDto);
    List<PaymentResponseDto> getPayments (Date dateStart, Date dateEnd);
    PaymentResponseDto getPaymentById (Long paymentId);
}
