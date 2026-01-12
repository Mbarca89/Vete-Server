package com.mbarca.vete.scheduler;

import com.mbarca.vete.service.WebOrderReconcileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebOrderReconcileScheduler {

    private final WebOrderReconcileService reconcileService;

    // cada 30 minutos (minuto 0 y 30)
    @Scheduled(cron = "0 0,30 * * * *")
    public void reconcile() {
        // Busca PENDING de hace +30min, y si no hay pago en MP expira a los 60min
        reconcileService.reconcilePendingOlderThanMinutes(30, 60);
    }
}
