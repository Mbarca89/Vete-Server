package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.repository.WebOrderItemRepository;
import com.mbarca.vete.repository.WebOrderRepository;
import com.mbarca.vete.service.MercadoPagoPaymentLookup;
import com.mbarca.vete.service.WebOrderReconcileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebOrderReconcileServiceImpl implements WebOrderReconcileService {

    private final WebOrderRepository webOrderRepository;
    private final WebOrderItemRepository webOrderItemRepository;
    private final ProductRepository productRepository;
    private final MercadoPagoPaymentLookup paymentLookup;

    @Override
    @Transactional
    public void reconcilePendingOlderThanMinutes(int minutes, int expireAfterMinutes) {
        List<WebOrder> pending = webOrderRepository.findPendingOlderThanMinutes(minutes);
        if (pending.isEmpty()) return;

        for (WebOrder order : pending) {
            try {
                // 1) Buscar pago por external_reference = orderId
                Optional<MercadoPagoPaymentLookup.PaymentInfo> opt =
                        paymentLookup.findLatestByExternalReference(order.getId().toString());

                if (opt.isPresent()) {
                    var p = opt.get();
                    String mpStatus = (p.status() == null) ? "" : p.status().toLowerCase(Locale.ROOT);

                    // guardo paymentId si aún no lo tenía (no rompe nada)
                    webOrderRepository.updatePaymentIdIfNull(order.getId(), p.paymentId());

                    if ("approved".equals(mpStatus)) {
                        int rows = webOrderRepository.updateStatusAndPaymentIfPending(order.getId(), "APPROVED", p.paymentId());
                        if (rows > 0) log.info("Reconcile: Order {} -> APPROVED (payment {})", order.getId(), p.paymentId());
                        continue;
                    }

                    if ("rejected".equals(mpStatus) || "cancelled".equals(mpStatus)) {
                        restoreStock(order.getId());
                        int rows = webOrderRepository.updateStatusAndPaymentIfPending(order.getId(), "REJECTED", p.paymentId());
                        if (rows > 0) log.warn("Reconcile: Order {} -> REJECTED (payment {})", order.getId(), p.paymentId());
                        continue;
                    }

                    // pending / in_process / authorized / etc -> lo dejamos PENDING
                    log.info("Reconcile: Order {} sigue PENDING (mpStatus={})", order.getId(), mpStatus);
                    continue;
                }

                // 2) Si NO hay pagos en MP para esa orden, expirar después de X min
                long ageMinutes = Duration.between(order.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(),Instant.now()).toMinutes();

                if (ageMinutes >= expireAfterMinutes) {
                    restoreStock(order.getId());
                    int rows = webOrderRepository.updateStatusIfPending(order.getId(), "CANCELLED_EXPIRED");
                    if (rows > 0) log.warn("Reconcile: Order {} -> CANCELLED_EXPIRED (age {} min)", order.getId(), ageMinutes);
                }

            } catch (Exception e) {
                log.error("Reconcile error for order {}", order.getId(), e);
            }
        }
    }

    private void restoreStock(Long orderId) {
        List<WebOrderItem> items = webOrderItemRepository.findByOrderId(orderId);
        for (WebOrderItem it : items) {
            productRepository.increaseStock(it.getProductId(), it.getQuantity());
        }
    }
}
