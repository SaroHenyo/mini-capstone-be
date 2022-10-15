package henyosisaro.minicapstonebe.controller;

import henyosisaro.minicapstonebe.dto.ProductDTO;
import henyosisaro.minicapstonebe.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/getProductByUser/{email}")
    public List<ProductDTO> getAllProductsByUser(@PathVariable String email) {
        return cartService.getAllProductsByUser(email);
    }

    @PutMapping("/{email}/addProduct/{productId}")
    public List<ProductDTO> addToCart(@PathVariable String email, @PathVariable UUID productId) {
        return cartService.addToCart(email, productId);
    }

    @PostMapping("/checkout/{email}")
    public List<ProductDTO> checkOut(@PathVariable String email) {
        return cartService.checkOut(email);
    }

}