package henyosisaro.minicapstonebe.model;

import lombok.*;

@Data
@Builder

public class ProductRequest {

    private String productName;
    private String imageLink;
    private float price;
    private float ratings;
    private String type;
    private String filter;
    private String description;

}

