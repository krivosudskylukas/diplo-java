package fei.stu.billing.infra.customer.mapper;

import fei.stu.billing.infra.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Integer> {
}
