package henyosisaro.minicapstonebe.repository;

import henyosisaro.minicapstonebe.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, BigInteger> {
    List<CartEntity> findAllByUserId(UUID userId);

    @Transactional
    void deleteAllByUserId(UUID uuid);
}

