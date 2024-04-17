package fei.stu.billing.app.job;

import fei.stu.billing.app.service.customer.CustomerComputerService;
import fei.stu.billing.app.service.invoice.InvoiceService;
import fei.stu.billing.app.service.mail.MailService;
import fei.stu.billing.domain.CustomerComputer;
import fei.stu.billing.infra.customer.repository.CustomerEntityMapper;
import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

import static fei.stu.billing.app.common.Constants.CAN_NOT_CONNECT_TO_CUSTOMER;

@Component
public class InvoiceProcessor {

    private final InvoiceService invoiceService;
    private final CustomerComputerService customerComputerService;
    private final MailService mailService;
    private final RestClient restClient;
    private final CustomerEntityMapper customerMapper;

    public InvoiceProcessor(InvoiceService invoiceService, CustomerComputerService customerComputerService, MailService mailService, RestClient restClient, CustomerEntityMapper customerMapper) {
        this.invoiceService = invoiceService;
        this.customerComputerService = customerComputerService;
        this.mailService = mailService;
        this.restClient = restClient;
        this.customerMapper = customerMapper;
    }

    // Job that query paid invoices and try to create new license file in remote pc

    @Transactional
    @Scheduled(fixedRate = 500000)
    public void processInvoices(){
        List<InvoiceEntity> paidInvoices = invoiceService.getPaidInvoices();

        for(InvoiceEntity paidInvoice: paidInvoices){
            CustomerComputer customerComputer = customerComputerService.getCustomerComputerInfo(paidInvoice.getCustomer().getId());

            if(paidInvoice.getNumberOfTries() > 5 && !paidInvoice.isFileTransferred()){
                String body = "Unable to connect to "
                        + customerComputer.url();
                mailService.sendEmail(customerMapper.mapFromEntity(paidInvoice.getCustomer()), CAN_NOT_CONNECT_TO_CUSTOMER, body);
                continue;
            }

            String response = createFileInRemotePc(customerComputer);
            if(!response.equals("1")){
                paidInvoice.setNumberOfTries(paidInvoice.getNumberOfTries() + 1);
                String body = "Unable to connect to "
                        + customerComputer.url();
                mailService.sendEmail(customerMapper.mapFromEntity(paidInvoice.getCustomer()), CAN_NOT_CONNECT_TO_CUSTOMER , body);
                invoiceService.saveInvoice(paidInvoice);
                continue;
            }

            paidInvoice.setNumberOfTries(paidInvoice.getNumberOfTries() + 1);
            paidInvoice.setFileTransferred(true);
            invoiceService.saveInvoice(paidInvoice);
        }
    }

    public String createFileInRemotePc(CustomerComputer customerComputer){
        ResponseEntity<String> response = restClient.get()
                .uri(customerComputer.url())
                .header("user_login","admin")
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }

}
