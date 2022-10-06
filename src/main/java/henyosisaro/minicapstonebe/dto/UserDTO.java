package henyosisaro.minicapstonebe.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String email;
    private int totalOrders;
    private int successOrders;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;
}

