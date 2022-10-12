package henyosisaro.minicapstonebe.controller;

import henyosisaro.minicapstonebe.dto.ProductDTO;
import henyosisaro.minicapstonebe.model.ProductRequest;
import henyosisaro.minicapstonebe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/getAll")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/add")
    public List<ProductDTO> addProduct(@RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @DeleteMapping("/delete/{productId}")
    public List<ProductDTO> deleteProduct(@PathVariable UUID productId) {
        return productService.deleteProduct(productId);
    }
}