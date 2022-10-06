package henyosisaro.minicapstonebe.entity;

import henyosisaro.minicapstonebe.config.SchemaConfiguration;
import lombok.*;
import org.hibernate.Hibernate;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = SchemaConfiguration.SCHEMA_NAME, name = "PRODUCTS")
public class ProductEntity {
    @Id
    private UUID productId;
    private String productName;
    private String imageLink;
    private float price;
    private float ratings;
    private String type;
    private String filter;
    private String description;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductEntity that = (ProductEntity) o;
        return productId != null && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

