package henyosisaro.minicapstonebe.service;

import henyosisaro.minicapstonebe.dto.ProductDTO;
import henyosisaro.minicapstonebe.entity.CartEntity;
import henyosisaro.minicapstonebe.entity.ProductEntity;
import henyosisaro.minicapstonebe.entity.UserEntity;
import henyosisaro.minicapstonebe.exception.UserAlreadyExist;
import henyosisaro.minicapstonebe.repository.CartRepository;
import henyosisaro.minicapstonebe.repository.ProductRepository;
import henyosisaro.minicapstonebe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<ProductDTO> getAllProductsByUser(String email) {

        // Get user details;
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) throw new UserAlreadyExist("User doesn't exist");

        // Get all products by userId
        List<CartEntity> allCartProducts = cartRepository.findAllByUserId(user.getUserId());

        // Initialize DTO
        List<ProductDTO> allCartProductsDTO  = new ArrayList<>();

        allCartProducts.forEach(cartProduct -> {
            // Get product details
            ProductEntity product = productRepository.findByProductId(cartProduct.getProductId());

            // Add product to DTO list
            allCartProductsDTO.add(modelMapper.map(product, ProductDTO.class));
        });

        return allCartProductsDTO;
    }

    public List<ProductDTO> addToCart(String email, UUID productId) {
        // Get user details
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) throw new UserAlreadyExist("User doesn't exist");

        // Get product details
        ProductEntity product = productRepository.findByProductId(productId);
        if(product == null) throw new UserAlreadyExist("Product doesn't exist");

        // Save to cart
        cartRepository.save(CartEntity.builder().productId(product.getProductId()).userId(user.getUserId()).build());

        return getAllProductsByUser(user.getEmail());
    }

    public List<ProductDTO> checkOut(String email) {
        // Get user details
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) throw new UserAlreadyExist("User doesn't exist");

        // Get all products by userId
        List<CartEntity> allCartProducts = cartRepository.findAllByUserId(user.getUserId());

        // Update user details
        user.setTotalOrders(user.getTotalOrders() + allCartProducts.size());
        user.setSuccessOrders(user.getSuccessOrders() + allCartProducts.size());

        // Delete all cart products
        cartRepository.deleteAllByUserId(user.getUserId());

        // Save to database
        userRepository.save(user);

        return getAllProductsByUser(user.getEmail());
    }
}