package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Bill;

import java.util.Date;
import java.util.List;

public interface BillRepository {
    Integer saveBill(Bill bill);

    Bill getBillById(Long id);

    List<Bill> getBillsByDate(Date dateStart, Date dateEnd);
}
