package fei.stu.billing.app.job;

import fei.stu.billing.app.service.customer.CustomerComputerService;
import fei.stu.billing.app.service.invoice.InvoiceService;
import fei.stu.billing.app.service.mail.MailService;
import fei.stu.billing.domain.CustomerComputer;
import fei.stu.billing.domain.Invoice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static fei.stu.billing.app.common.Constants.SUBJECT;

@Component
public class InvoiceProcessor {

    private final InvoiceService invoiceService;
    private final CustomerComputerService customerComputerService;
    private final MailService mailService;

    public InvoiceProcessor(InvoiceService invoiceService, CustomerComputerService customerComputerService, MailService mailService) {
        this.invoiceService = invoiceService;
        this.customerComputerService = customerComputerService;
        this.mailService = mailService;
    }


    @Transactional
    @Scheduled(fixedRate = 500000)
    public void processInvoices(){
        List<Invoice> unpaidInvoices = invoiceService.getUnpaidInvoices();

        for(Invoice unpaidInvoice: unpaidInvoices){
            if(unpaidInvoice.numberOfTries() > 5){
                String body = "Customer "
                        + unpaidInvoice.customer().companyName()
                        + " has not paid invoice from "
                        + unpaidInvoice.dueDate()+ " yet.";
                mailService.sendEmail(unpaidInvoice.customer(), SUBJECT, body);
            }
            CustomerComputer customerComputer = customerComputerService.getCustomerComputerInfo(unpaidInvoice.customer());
            
        }
    }
}
