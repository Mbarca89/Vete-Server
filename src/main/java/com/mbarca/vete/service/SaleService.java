package com.mbarca.vete.service;

import com.mbarca.vete.domain.MonthlyReport;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.CategoryTotalResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;

import java.util.Date;
import java.util.List;

public interface SaleService {
    String createSale (SaleRequestDto saleRequestDto);
    SaleResponseDto getSaleById (Long saleId);
    List<SaleResponseDto> getSalesByDate (Date dateStart, Date dateEnd);
    List<CategoryTotalResponseDto> getSalesByCategory (Date dateStart, Date dateEnd);
    MonthlyReport getSalesReport(Date dateStart, Date dateEnd);
}
