package com.mbarca.vete.controller;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.dto.response.WebOrderStatusResponse;
import com.mbarca.vete.repository.WebOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/web-orders/public")
@RequiredArgsConstructor
public class WebOrderPublicController {

    private final WebOrderRepository webOrderRepository;

    @GetMapping("/{id}")
    public WebOrderStatusResponse getStatus(@PathVariable Long id) {
        WebOrder o = webOrderRepository.findById(id);
        return new WebOrderStatusResponse(
                o.getId(),
                o.getStatus(),
                o.getPaymentId(),
                o.getPreferenceId(),
                o.getTotalAmount()
        );
    }
}
