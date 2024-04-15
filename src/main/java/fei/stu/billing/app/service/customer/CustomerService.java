package fei.stu.billing.app.service.customer;

import fei.stu.billing.infra.customer.mapper.CustomerJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {
    private final CustomerJpaRepository customerRepository;

    public CustomerService(CustomerJpaRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

}
