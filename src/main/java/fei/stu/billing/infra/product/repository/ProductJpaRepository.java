package fei.stu.billing.infra.product.repository;

import fei.stu.billing.infra.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Integer> {
}
