package fei.stu.billing.infra.invoice.repository;

import fei.stu.billing.infra.invoice.entity.InvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailJpaRepository extends JpaRepository<InvoiceDetailEntity, Integer> {
}
