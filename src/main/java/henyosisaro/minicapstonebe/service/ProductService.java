package henyosisaro.minicapstonebe.service;

import henyosisaro.minicapstonebe.dto.ProductDTO;
import henyosisaro.minicapstonebe.entity.ProductEntity;
import henyosisaro.minicapstonebe.exception.UserAlreadyExist;
import henyosisaro.minicapstonebe.model.ProductRequest;
import henyosisaro.minicapstonebe.repository.ProductRepository;
import henyosisaro.minicapstonebe.util.DateTimeUtil;
import henyosisaro.minicapstonebe.util.S3StorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;
    private final S3StorageUtil s3StorageUtil;

    public List<ProductDTO> getAllProducts() {

        // Get all data from database
        List<ProductEntity> allProducts = productRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<ProductDTO> allProductsDTO = new ArrayList<>();

        allProducts.forEach(product -> {
            allProductsDTO.add(modelMapper.map(product, ProductDTO.class));
        });

        return allProductsDTO;
    }

    public ProductDTO getProduct(UUID productId) {

        // Get product from database
        ProductEntity product = productRepository.findByProductId(productId);

        if (product == null) throw new UserAlreadyExist("Product doesn't exist");

        return modelMapper.map(product, ProductDTO.class);
    }

    public List<ProductDTO> addProduct(ProductRequest newProduct) {

        // Save new product to database
        productRepository.save(ProductEntity
                .builder()
                .productId(UUID.randomUUID())
                .productName(newProduct.getProductName())
                .imageLink(null)
                .price(newProduct.getPrice())
                .ratings(newProduct.getRatings())
                .type(newProduct.getType())
                .filter(newProduct.getFilter())
                .description(newProduct.getDescription())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllProducts();
    }

    public List<ProductDTO> deleteProduct(UUID productId) {

        // Get product
        ProductEntity product = productRepository.findByProductId(productId);

        // Check if product exist
        if(product == null) throw new UserAlreadyExist("Product doesn't exist");

        // Delete product
        productRepository.deleteByProductId(productId);

        return getAllProducts();
    }

    public List<ProductDTO> uploadProductImage(UUID productId, MultipartFile file) {
        // Initialize product
        ProductEntity product = productRepository.findByProductId(productId);
        if(product == null) throw new IllegalStateException("Product doesn't exist");

        // Check if file validity
        s3StorageUtil.checkFile(file);

        // Grab some meta data
        Map<String, String> metadata = s3StorageUtil.getMetaData(file);

        // Store the image to s3 bucket
        String path = String.format("%s/%s", "minicapstone-mikez/saro/products", productId);
        String fileName = String.format("%s-%s", "product", file.getOriginalFilename());

        try {
            s3StorageUtil.save(path, fileName, Optional.of(metadata), file.getInputStream());
            productRepository.save(ProductEntity
                    .builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .imageLink(fileName)
                    .price(product.getPrice())
                    .ratings(product.getRatings())
                    .type(product.getType())
                    .filter(product.getFilter())
                    .description(product.getDescription())
                    .createdDate(product.getCreatedDate())
                    .modifiedDate(dateTimeUtil.currentDate())
                    .build());
        } catch (IOException e) {
            throw  new IllegalStateException(e);
        }

        return getAllProducts();
    }

    public byte[] downloadProductImage(UUID productId) {
        // Initialize product
        ProductEntity product = productRepository.findByProductId(productId);
        if(product == null) throw new IllegalStateException("Product doesn't exist");

        String path = String.format("%s/%s", "minicapstone-mikez/saro/products", productId);

        return s3StorageUtil.download(path, product.getImageLink());
    }
}