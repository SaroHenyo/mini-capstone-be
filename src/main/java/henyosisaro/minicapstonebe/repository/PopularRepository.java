package henyosisaro.minicapstonebe.repository;


import henyosisaro.minicapstonebe.entity.PopularEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.UUID;

public interface PopularRepository extends JpaRepository<PopularEntity, BigInteger> {
    PopularEntity findByProductId(UUID productId);

    @Transactional
    void deleteByProductId(UUID productId);
}