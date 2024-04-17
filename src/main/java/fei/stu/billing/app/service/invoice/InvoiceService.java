package fei.stu.billing.app.service.invoice;

import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import fei.stu.billing.infra.invoice.mapper.InvoiceEntityMapper;
import fei.stu.billing.infra.invoice.repository.InvoiceJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static fei.stu.billing.app.common.Constants.FILE_NOT_TRANSFERRED;
import static fei.stu.billing.app.common.Constants.PAID_INVOICE;

@Component
public class InvoiceService {

    private final InvoiceJpaRepository repository;
    private final InvoiceEntityMapper mapper;

    public InvoiceService(InvoiceJpaRepository repository, InvoiceEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<InvoiceEntity> getPaidInvoices(){
        return repository
                .findByPaidAndDate(PAID_INVOICE, LocalDateTime.now(), FILE_NOT_TRANSFERRED)
                .stream()
                .toList();
    }

    public void saveInvoice(InvoiceEntity entity){
        repository.save(entity);
    }
}
