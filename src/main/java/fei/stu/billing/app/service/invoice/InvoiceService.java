package fei.stu.billing.app.service.invoice;

import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import fei.stu.billing.infra.invoice.mapper.InvoiceEntityMapper;
import fei.stu.billing.infra.invoice.repository.InvoiceJpaRepository;
import fei.stu.billing.web.dto.Response;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static fei.stu.billing.app.common.Constants.FILE_NOT_TRANSFERRED;
import static fei.stu.billing.app.common.Constants.PAID_INVOICE;
import static fei.stu.billing.app.common.Constants.UNPAID_INVOICE;

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

    public List<InvoiceEntity> getUnpaidInvoices(){
        return repository
                .findUnpaid(UNPAID_INVOICE, LocalDateTime.now())
                .stream()
                .toList();
    }

    public void saveInvoice(InvoiceEntity entity){
        repository.save(entity);
    }

    public Response checkIfInvoiceIsPaid(Integer customerId) throws Exception {
        InvoiceEntity currMonthInvoice = repository.getCurrMonthInvoice(customerId,LocalDateTime.now());
        if(!currMonthInvoice.isPaid()){
            throw new Exception("Invoice is not paid");
        }
        List<String> functionality = currMonthInvoice.getInvoiceDetails().stream().map(entity -> entity.getProduct().getProductName()).toList();
        return new Response(getNextMonth(), currMonthInvoice.getCustomer().getCompanyName(), getNow(), functionality);
    }

    private long getNextMonth(){
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Adjust to the first of the next month
        LocalDateTime firstOfNextMonth = now.with(TemporalAdjusters.firstDayOfNextMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Convert LocalDateTime to ZonedDateTime to include the time zone
        ZonedDateTime zonedDateTime = firstOfNextMonth.atZone(ZoneId.systemDefault());

        // Convert to epoch seconds
        return zonedDateTime.toEpochSecond();
    }

    private long getNow(){
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Convert LocalDateTime to ZonedDateTime to include the time zone
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());

        // Convert to epoch seconds
        return zonedDateTime.toEpochSecond();
    }
}
