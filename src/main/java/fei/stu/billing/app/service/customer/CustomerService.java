package fei.stu.billing.app.service.customer;

import fei.stu.billing.app.service.mail.MailService;
import fei.stu.billing.infra.customer.entity.CustomerEntity;
import fei.stu.billing.infra.customer.mapper.CustomerJpaRepository;
import fei.stu.billing.infra.customer.repository.CustomerEntityMapper;
import fei.stu.billing.web.dto.CustomerInfoDto;
import org.springframework.stereotype.Component;

import static fei.stu.billing.app.common.Constants.INVALID_CONFIGURATION;
import static fei.stu.billing.app.common.Constants.INVALID_SIGNATURE;

@Component
public class CustomerService {
    private final CustomerJpaRepository customerRepository;
    private final MailService mailService;
    private final CustomerEntityMapper customerMapper;

    public CustomerService(CustomerJpaRepository customerRepository, MailService mailService, CustomerEntityMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.mailService = mailService;
        this.customerMapper = customerMapper;
    }

    public void notifyMe(CustomerInfoDto customerRequest){
        CustomerEntity customer = customerRepository.findById(customerRequest.customerId()).orElse(null);
        if(customer == null){
            String body = "Customer id["
                    + customerRequest.customerId() + "] that was sent in request is not valid." +
                    " Invalid configuration in " + customerRequest.customerName() + ". Customer tried to mess with data!";
            mailService.sendEmailToMe(INVALID_CONFIGURATION, body);
            throw new RuntimeException("Customer has invalid configuration");
        }
        String body = "Customer " + customerRequest.customerName() +
                " was tampering with the data. Signature is no longer valid.";
        mailService.sendEmailToMe(INVALID_SIGNATURE, body);
    }
}
