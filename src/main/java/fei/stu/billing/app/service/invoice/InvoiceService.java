package fei.stu.billing.app.service.invoice;

import fei.stu.billing.domain.Invoice;
import fei.stu.billing.infra.invoice.mapper.InvoiceEntityMapper;
import fei.stu.billing.infra.invoice.repository.InvoiceJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceService {

    private final InvoiceJpaRepository repository;
    private final InvoiceEntityMapper mapper;

    public InvoiceService(InvoiceJpaRepository repository, InvoiceEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Invoice> getUnpaidInvoices(){
        return repository
                .findByPaid(false)
                .stream()
                .map(mapper::mapFromEntity)
                .toList();
    }
}
