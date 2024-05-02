package com.mbarca.vete.repository;

import com.mbarca.vete.domain.CategoryTotal;
import com.mbarca.vete.domain.Sale;

import java.util.Date;
import java.util.List;

public interface SaleRepository {
    Integer createSale (Sale sale);
    Sale getSaleWithProductsById(long saleId);
    List<Sale> getSalesByDate(Date dateStart, Date dateEnd);
    List<CategoryTotal> getSalesByCategory();
}
