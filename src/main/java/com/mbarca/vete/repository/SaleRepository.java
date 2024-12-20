package com.mbarca.vete.repository;

import com.mbarca.vete.domain.CategoryTotal;
import com.mbarca.vete.domain.MonthlyReport;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.dto.response.CombinedReport;
import com.mbarca.vete.dto.response.SimplifiedReport;

import java.util.Date;
import java.util.List;

public interface SaleRepository {
    Integer createSale (Sale sale);
    Sale getSaleWithProductsById(long saleId);
    List<Sale> getSalesByDate(Date dateStart, Date dateEnd);
    List<CategoryTotal> getSalesByCategory(Date dateStart, Date dateEnd);
    MonthlyReport getSalesReport(Date dateStart, Date dateEnd);
    CombinedReport getCombinedReport (Date dateStart, Date dateEnd);
    SimplifiedReport getSimplifiedReport (Date dateStart, Date dateEnd);
}
