package fei.stu.billing.infra.customer.mapper;

import fei.stu.billing.infra.customer.entity.CustomerComputerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerComputerJpaRepository extends JpaRepository<CustomerComputerEntity, Integer> {

    Optional<CustomerComputerEntity> findByCustomerId(Integer customerId);
}
