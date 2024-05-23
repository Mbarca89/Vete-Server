package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Payment;
import com.mbarca.vete.dto.request.PaymentRequestDto;
import com.mbarca.vete.dto.response.PaymentResponseDto;
import com.mbarca.vete.repository.PaymentRepository;
import com.mbarca.vete.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String createPayment(PaymentRequestDto paymentRequestDto) {
        Integer response = paymentRepository.createPayment(mapDtoToPayment(paymentRequestDto));
        if (response.equals(0)) {
            return "Error al crear el pago!";
        }
        return "Pago creado correctamente!";
    }

    @Override
    public String makePayment(PaymentRequestDto paymentRequestDto) {
        Integer response = paymentRepository.makePayment(mapDtoToPayment(paymentRequestDto));
        if (response.equals(0)) {
            return "Error al modificar el pago!";
        }
        return "Pago modificado correctamente!";
    }

    @Override
    public List<PaymentResponseDto> getPayments(Date dateStart, Date dateEnd) {
        List<Payment> payments = paymentRepository.getPayments(dateStart, dateEnd);
        return payments.stream().map(this::mapPaymentToDto).collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId) {
        return mapPaymentToDto(paymentRepository.getPaymentById(paymentId));
    }

    private Payment mapDtoToPayment (PaymentRequestDto paymentRequestDto) {
        Payment payment = new Payment();
        payment.setId(paymentRequestDto.getId());
        payment.setBillNumber(paymentRequestDto.getBillNumber());
        payment.setAmount(paymentRequestDto.getAmount());
        payment.setProvider(paymentRequestDto.getProvider());
        payment.setPayed(paymentRequestDto.getPayed());
        payment.setPaymentMethod(paymentRequestDto.getPaymentMethod());
        return payment;
    }

    private PaymentResponseDto mapPaymentToDto (Payment payment) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(payment.getId());
        paymentResponseDto.setDate(payment.getDate());
        paymentResponseDto.setAmount(payment.getAmount());
        paymentResponseDto.setBillNumber(payment.getBillNumber());
        paymentResponseDto.setPayed(payment.getPayed());
        paymentResponseDto.setProvider(payment.getProvider());
        paymentResponseDto.setPaymentMethod(payment.getPaymentMethod());
        paymentResponseDto.setPaymentDate(payment.getPaymentDate());
        return paymentResponseDto;
    }
}
