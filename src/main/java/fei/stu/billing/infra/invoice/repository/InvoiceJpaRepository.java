package fei.stu.billing.infra.invoice.repository;

import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Integer> {

    List<InvoiceEntity> findByCustomerId(Integer customerId);

    List<InvoiceEntity> findByPaid(boolean paid);
}
