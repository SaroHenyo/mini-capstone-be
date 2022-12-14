package henyosisaro.minicapstonebe.controller;

import henyosisaro.minicapstonebe.dto.PopularDTO;
import henyosisaro.minicapstonebe.model.PopularRequest;
import henyosisaro.minicapstonebe.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/popular")
@RequiredArgsConstructor
public class PopularController {

    private final PopularService popularService;

    @GetMapping("/getAll")
    public List<PopularDTO> getAllPopularProducts() {
        return popularService.getAllPopularProducts();
    }

    @PutMapping("/add")
    public List<PopularDTO> addPopular(@RequestBody PopularRequest popularRequest) {
        return popularService.addPopular(popularRequest);
    }

    @DeleteMapping("/delete/{productId}")
    public List<PopularDTO> deletePopular(@PathVariable UUID productId) {
        return popularService.deletePopular(productId);
    }

    @PutMapping(path = "/{productId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PopularDTO> uploadProductImage(@PathVariable UUID productId, @RequestParam("file") MultipartFile file) {
        return popularService.uploadProductImage(productId, file);
    }

    @GetMapping(path = "{productId}/download")
    public byte[] downloadProductImage(@PathVariable("productId") UUID userProfileId) {
        return popularService.downloadProductImage(userProfileId);
    }

}