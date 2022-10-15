package henyosisaro.minicapstonebe.service;

import henyosisaro.minicapstonebe.dto.PopularDTO;
import henyosisaro.minicapstonebe.entity.PopularEntity;
import henyosisaro.minicapstonebe.exception.UserAlreadyExist;
import henyosisaro.minicapstonebe.model.PopularRequest;
import henyosisaro.minicapstonebe.repository.PopularRepository;
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
public class PopularService {

    private final PopularRepository popularRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;
    private final S3StorageUtil s3StorageUtil;

    public List<PopularDTO> getAllPopularProducts() {

        // Get all data from database
        List<PopularEntity> allPopularProducts = popularRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<PopularDTO> allPopularProductsDTO = new ArrayList<>();

        allPopularProducts.forEach(product -> {
            allPopularProductsDTO.add(modelMapper.map(product, PopularDTO.class));
        });

        return allPopularProductsDTO;
    }

    public List<PopularDTO> addPopular(PopularRequest newPopular) {

        // Save new blog to database
        popularRepository.save(PopularEntity
                .builder()
                .productId(UUID.randomUUID())
                .productName(newPopular.getProductName())
                .imageLink(null)
                .price(newPopular.getPrice())
                .type(newPopular.getType())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllPopularProducts();
    }

    public List<PopularDTO> deletePopular(UUID productId) {

        // Get popular product
        PopularEntity popular = popularRepository.findByProductId(productId);

        // Check if popular product exist
        if(popular == null) throw new UserAlreadyExist("Popular product doesn't exist");

        // Delete popular product
        popularRepository.deleteByProductId(productId);

        return getAllPopularProducts();
    }

    public List<PopularDTO> uploadProductImage(UUID productId, MultipartFile file) {
        // Initialize product
        PopularEntity product = popularRepository.findByProductId(productId);
        if (product == null) throw new IllegalStateException("Product doesn't exist");


        // Check if file validity
        s3StorageUtil.checkFile(file);

        // Grab some meta data
        Map<String, String> metadata = s3StorageUtil.getMetaData(file);

        // Store the image in S3
        String path = String.format("%s/%s", "minicapstone-mikez/saro/popular-products", productId);
        String fileName = String.format("%s-%s", "popular-product", file.getOriginalFilename());
        try {
            s3StorageUtil.save(path, fileName, Optional.of(metadata), file.getInputStream());
            popularRepository.save(PopularEntity
                    .builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .imageLink(fileName)
                    .price(product.getPrice())
                    .type(product.getType())
                    .createdDate(product.getCreatedDate())
                    .modifiedDate(dateTimeUtil.currentDate())
                    .build());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return getAllPopularProducts();
    }

    public byte[] downloadProductImage(UUID productId) {
        // Initialize product
        PopularEntity product = popularRepository.findByProductId(productId);
        if (product == null) throw new IllegalStateException("Product doesn't exist");

        String path = String.format("%s/%s", "minicapstone-mikez/saro/popular-products", productId);

        return s3StorageUtil.download(path, product.getImageLink());
    }
}