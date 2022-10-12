package henyosisaro.minicapstonebe.repository;

import henyosisaro.minicapstonebe.entity.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, BigInteger> {
    BlogEntity findByBlogId(UUID blogId);

    @Transactional
    void deleteByBlogId(UUID blogId);
}