package henyosisaro.minicapstonebe.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import henyosisaro.minicapstonebe.dto.ProductDTO;
import henyosisaro.minicapstonebe.entity.ProductEntity;
import henyosisaro.minicapstonebe.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

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
}
