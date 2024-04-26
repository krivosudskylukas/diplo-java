package fei.stu.billing.infra.invoice.repository;

import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Integer> {

    List<InvoiceEntity> findByCustomerId(Integer customerId);

    List<InvoiceEntity> findByPaid(boolean paid);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.paid = :paid AND i.dueDate > :time and i.fileTransferred = :fileTransferred")
    List<InvoiceEntity> findByPaidAndDate(@Param("paid")boolean paid, @Param("time")LocalDateTime time, @Param("fileTransferred")boolean fileTransferred);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.paid = :paid AND i.dueDate > :time")
    List<InvoiceEntity> findUnpaid(@Param("paid")boolean paid, @Param("time")LocalDateTime time);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.customer = :customerId AND i.dueDate > :time")
    InvoiceEntity getCurrMonthInvoice(@Param("customerId")Integer customerId, @Param("time")LocalDateTime time);

}
