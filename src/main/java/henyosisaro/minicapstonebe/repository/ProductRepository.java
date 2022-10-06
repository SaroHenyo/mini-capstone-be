package henyosisaro.minicapstonebe.repository;

import henyosisaro.minicapstonebe.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, BigInteger> {
}
