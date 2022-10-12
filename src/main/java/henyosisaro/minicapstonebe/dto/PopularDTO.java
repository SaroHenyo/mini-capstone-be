package henyosisaro.minicapstonebe.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class PopularDTO {

    private UUID productId;
    private String productName;
    private String imageLink;
    private float price;
    private String type;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;
}
