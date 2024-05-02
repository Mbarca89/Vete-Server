package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Product;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.NotFoundException;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String createProduct(ProductRequestDto productRequestDto, byte[] compressedImage) throws MissingDataException {
        if (productRequestDto.getName() == null ||
                productRequestDto.getCost() == null ||
                productRequestDto.getPrice() == null ||
                productRequestDto.getStock() == null ||
                productRequestDto.getCategoryName() == null ||
                Objects.equals(productRequestDto.getName(), "") ||
                Objects.equals(productRequestDto.getCategoryName(), "") ||
                Objects.equals(productRequestDto.getProviderName(), "")
        ) {
            throw new MissingDataException("Faltan datos!");
        }

        Product product = mapDtoToProduct(productRequestDto);
        product.setPhoto(compressedImage);
        Integer response = productRepository.createProduct(product);
        if (response.equals(0)) {
            return "Error al crear el producto!";
        }
        return "Producto creado correctamente!";
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getByCategory(String categoryName, int page, int size) {
        int offset = (page - 1) * size;
        List<Product> products = productRepository.getByCategory(categoryName, size, offset);
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    @Override
    public Integer getProductCount() {
        return productRepository.getProductCount();
    }
    @Override
    public Integer getCategoryCount(String categoryName) {
        return productRepository.getCategoryCount(categoryName);
    }

    @Override
    public List<ProductResponseDto> getProductsPaginated(int page, int size) {
        int offset = (page - 1) * size;
        List<Product> products = productRepository.getProductsPaginated(size, offset);
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    @Override
    public String editProduct (ProductRequestDto productRequestDto, byte[] compressedImage) throws Exception {
        if(Objects.equals(productRequestDto.getName(), "") ||
        productRequestDto.getStock() == null ||
        productRequestDto.getCost() == null ||
        productRequestDto.getPrice() == null ||
                Objects.equals(productRequestDto.getProviderName(), "") ||
                Objects.equals(productRequestDto.getCategoryName(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        Product product = mapDtoToProduct(productRequestDto);
        if(compressedImage != null) product.setPhoto(compressedImage);
        Integer response = productRepository.editProduct(product);

        if (response.equals(0)) {
            throw new Exception("Error al editar el producto!");
        }
        return "Producto editado correctamente!";
    }

    @Override
    public String deleteProduct (Long productId) throws Exception {
        int response = productRepository.deleteProduct(productId);
        if (response == 0) {
            throw new Exception("Error al eliminar el producto");
        }
        return "Producto eliminado correctamente";
    }

    @Override
    public List<ProductResponseDto> searchProduct(String searchTerm) {
        List<Product> products = productRepository.searchProduct(searchTerm);
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsFromProvider (Long providerId) {
        List <Product> products = productRepository.getProductsFromProvider(providerId);
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    public byte[] compressImage(byte[] imageData) throws IOException, MaxUploadSizeExceededException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageData)), "jpg", outputStream);
        return outputStream.toByteArray();
    }

    private Product mapDtoToProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setId(productRequestDto.getId());
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setBarCode(productRequestDto.getBarCode());
        product.setCost(productRequestDto.getCost());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
        product.setCategoryName(productRequestDto.getCategoryName());
        product.setProviderName(productRequestDto.getProviderName());
        return product;
    }

    private ProductResponseDto mapProductToDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setBarCode(product.getBarCode());
        productResponseDto.setCost(product.getCost());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setStock(product.getStock());
        productResponseDto.setCategoryName(product.getCategoryName());
        productResponseDto.setImage(product.getPhoto());
        productResponseDto.setProviderName(product.getProviderName());
        return productResponseDto;
    }

}
