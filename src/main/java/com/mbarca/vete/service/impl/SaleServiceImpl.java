package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.CategoryTotal;
import com.mbarca.vete.domain.MonthlyReport;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.CategoryTotalResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.repository.SaleRepository;
import com.mbarca.vete.service.SaleService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {
    SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public String createSale(SaleRequestDto saleRequestDto) {
        Integer response = saleRepository.createSale(mapDtoToSale(saleRequestDto));
        if (response.equals(0)) {
            return "Error al crear la venta!";
        }
        return "Venta creada correctamente!";
    }

    @Override
    public SaleResponseDto getSaleById(Long saleId) {
        return mapSaleToDto(saleRepository.getSaleWithProductsById(saleId));
    }

    @Override
    public List<SaleResponseDto> getSalesByDate (Date dateStart, Date dateEnd) {
        List<Sale> sales = saleRepository.getSalesByDate(dateStart,dateEnd);
        return sales.stream().map(this::mapSaleToDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryTotalResponseDto> getSalesByCategory (Date dateStart, Date dateEnd) {
        List<CategoryTotal> categoryTotals = saleRepository.getSalesByCategory(dateStart, dateEnd);
        return categoryTotals.stream().map(this::mapSaleByCategoryToDto).collect(Collectors.toList());
    }

    @Override
    public MonthlyReport getSalesReport(Date dateStart, Date dateEnd) {
        return saleRepository.getSalesReport(dateStart, dateEnd);
    }

    private Sale mapDtoToSale(SaleRequestDto saleRequestDto) {
        Sale sale = new Sale();
        sale.setDate(new Date());
        sale.setAmount(saleRequestDto.getAmount());
        sale.setCost(saleRequestDto.getCost());
        sale.setSeller(saleRequestDto.getSeller());
        sale.setDiscount(saleRequestDto.isDiscount());
        sale.setDiscountAmount(saleRequestDto.getDiscountAmount());
        sale.setSaleProducts(saleRequestDto.getSaleProducts());
        return sale;
    }

    private SaleResponseDto mapSaleToDto (Sale sale) {
        SaleResponseDto saleResponseDto = new SaleResponseDto();
        saleResponseDto.setId(sale.getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(sale.getDate());
        saleResponseDto.setDate(formattedDate);

        saleResponseDto.setAmount(sale.getAmount());
        saleResponseDto.setCost(sale.getCost());
        saleResponseDto.setSeller(sale.getSeller());
        saleResponseDto.setDiscount(sale.isDiscount());
        saleResponseDto.setDiscountAmount(sale.getDiscountAmount());
        saleResponseDto.setSaleProducts(sale.getSaleProducts());
        return saleResponseDto;
    }

    private CategoryTotalResponseDto mapSaleByCategoryToDto (CategoryTotal categoryTotal) {
        CategoryTotalResponseDto categoryTotalResponseDto = new CategoryTotalResponseDto();
        categoryTotalResponseDto.setCategoryName(categoryTotal.getCategoryName());
        categoryTotalResponseDto.setTotalAmount(categoryTotal.getTotalAmount());
        return categoryTotalResponseDto;
    }
}
