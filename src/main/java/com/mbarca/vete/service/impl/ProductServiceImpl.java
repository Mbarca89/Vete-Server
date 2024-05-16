package com.mbarca.vete.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.StockAlert;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.dto.response.StockAlertResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.NotFoundException;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
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
        product.setImage(compressedImage);
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
    public PaginatedResults<ProductResponseDto> getByCategory(String categoryName, int page, int size) {
        int offset = (page - 1) * size;
        PaginatedResults<Product> products = productRepository.getByCategory(categoryName, size, offset);
        List<ProductResponseDto> productResponseDtos = products.getData().stream().map(this::mapProductToDto).toList();
        return new PaginatedResults<ProductResponseDto>(productResponseDtos, products.getTotalCount());
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
    public PaginatedResults<ProductResponseDto> getProductsPaginated(int page, int size) {
        int offset = (page - 1) * size;
        PaginatedResults<Product> products = productRepository.getProductsPaginated(size, offset);
        List<ProductResponseDto> productResponseDtos = products.getData().stream().map(this::mapProductToDto).toList();
        return new PaginatedResults<ProductResponseDto>(productResponseDtos, products.getTotalCount());
    }

    @Override
    public String editProduct(ProductRequestDto productRequestDto, byte[] compressedImage) throws Exception {
        if (Objects.equals(productRequestDto.getName(), "") ||
                productRequestDto.getStock() == null ||
                productRequestDto.getCost() == null ||
                productRequestDto.getPrice() == null ||
                Objects.equals(productRequestDto.getCategoryName(), "")) {

            throw new MissingDataException("Faltan datos!");
        }

        Product product = mapDtoToProduct(productRequestDto);
        if (compressedImage != null) product.setImage(compressedImage);
        Integer response = productRepository.editProduct(product);

        if (response.equals(0)) {
            throw new Exception("Error al editar el producto!");
        }
        return "Producto editado correctamente!";
    }

    @Override
    public String deleteProduct(Long productId) throws Exception {
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
    public List<ProductResponseDto> getProductsFromProvider(Long providerId) {
        List<Product> products = productRepository.getProductsFromProvider(providerId);
        return products.stream().map(this::mapProductToDto).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long productId) throws NotFoundException {
        Product product = productRepository.getProductById(productId);
        return mapProductToDto(product);
    }

    @Override
    public List<StockAlertResponseDto> getStockAlerts() {
        List<StockAlert> stockAlerts = productRepository.getStockAlerts();
        return stockAlerts.stream().map(this::mapStockAlertsToDto).collect(Collectors.toList());
    }

    @Override
    public byte[] compressImage(byte[] imageData) throws IOException, MaxUploadSizeExceededException, ImageProcessingException, MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
        ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        int orientation = exifDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        if (orientation != 1) {
            AffineTransform transform = new AffineTransform();
            switch (orientation) {
                case 6:
                    transform.translate(originalImage.getHeight(), 0);
                    transform.rotate(Math.toRadians(90));
                    break;
                case 3:
                    transform.translate(originalImage.getWidth(), originalImage.getHeight());
                    transform.rotate(Math.toRadians(180));
                    break;
                case 8:
                    transform.translate(0, originalImage.getWidth());
                    transform.rotate(Math.toRadians(270));
                    break;
            }
            originalImage = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
                    .filter(originalImage, null);
        }
        BufferedImage outputImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        outputImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", outputStream);
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
        product.setStockAlert(productRequestDto.getStockAlert());
        product.setPublished(productRequestDto.getPublished());
        return product;
    }

    private StockAlertResponseDto mapStockAlertsToDto(StockAlert stockAlert) {
        StockAlertResponseDto stockAlertResponseDto = new StockAlertResponseDto();
        stockAlertResponseDto.setProductName(stockAlert.getProductName());
        stockAlertResponseDto.setStock(stockAlert.getStock());
        return stockAlertResponseDto;
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
        productResponseDto.setImage(product.getImage());
        productResponseDto.setProviderName(product.getProviderName());
        productResponseDto.setStockAlert(product.getStockAlert());
        productResponseDto.setPublished(product.getPublished());
        return productResponseDto;
    }

}
