package com.mbarca.vete.service;

import com.mbarca.vete.domain.AfipResponse;
import com.mbarca.vete.domain.Bill;
import com.mbarca.vete.dto.request.BillRequestDto;
import com.mbarca.vete.dto.request.SaleRequestDto;

import java.util.Date;
import java.util.List;

public interface BillService {
    String saveBill (BillRequestDto billRequestDto, AfipResponse afipResponse);
    Bill getByllById (Long id);
    List<Bill> getBillsByDate (Date dateStart, Date dateEnd);
}
