package com.mbarca.vete.service;

public interface WebOrderReconcileService {
    void reconcilePendingOlderThanMinutes(int minutes, int expireAfterMinutes);
}