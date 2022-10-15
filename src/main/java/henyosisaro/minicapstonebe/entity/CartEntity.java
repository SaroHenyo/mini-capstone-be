package henyosisaro.minicapstonebe.entity;


import henyosisaro.minicapstonebe.config.SchemaConfiguration;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = SchemaConfiguration.SCHEMA_NAME, name = "CART")
@IdClass(CartEntity.class)
public class CartEntity implements Serializable {
    @Id
    private UUID productId;

    @Id
    private UUID userId;
}