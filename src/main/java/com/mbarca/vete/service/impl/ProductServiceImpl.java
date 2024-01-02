package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Product;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String createProduct(ProductRequestDto productRequestDto) throws MissingDataException {
        if(productRequestDto.getName() == null ||
        productRequestDto.getCost() == null ||
        productRequestDto.getPrice() == null ||
        productRequestDto.getStock() == null ||
        productRequestDto.getCategoryName() == null ||
        productRequestDto.getSeller() == null ||
                Objects.equals(productRequestDto.getName(),"") ||
        Objects.equals(productRequestDto.getCategoryName(),"") ||
                Objects.equals(productRequestDto.getProvider(),"")
        ) {
            throw new MissingDataException("Faltan datos!");
        }

        Product product = mapDtoToProduct(productRequestDto);
        Integer response = productRepository.createProduct(product);
        if (response.equals(0)){
            return "Error al crear el producto!";
        }
        return "Producto creado correctamente!";
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    private Product mapDtoToProduct(ProductRequestDto productRequestDto){

        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setCost(productRequestDto.getCost());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
        product.setCategoryName(productRequestDto.getCategoryName());
        product.setSeller(productRequestDto.getSeller());
        product.setProvider(productRequestDto.getProvider());
        return product;
    }

    private ProductResponseDto mapProductToDto (Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setCost(product.getCost());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategoryName(product.getCategoryName());
        productResponseDto.setProvider(product.getProvider());
        return productResponseDto;
    }
}
