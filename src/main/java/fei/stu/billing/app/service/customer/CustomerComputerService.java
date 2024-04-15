package fei.stu.billing.app.service.customer;

import fei.stu.billing.domain.Customer;
import fei.stu.billing.domain.CustomerComputer;
import fei.stu.billing.infra.customer.repository.CustomerComputerEntityMapper;
import fei.stu.billing.infra.customer.mapper.CustomerComputerJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerComputerService {

    private final CustomerComputerJpaRepository repository;
    private final CustomerComputerEntityMapper mapper;

    public CustomerComputerService(CustomerComputerJpaRepository repository, CustomerComputerEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CustomerComputer getCustomerComputerInfo(Customer customer){
        return mapper.mapFromEntity(
                repository
                        .findByCustomerId(customer.id())
                        .orElseThrow(() -> new IllegalArgumentException("No customer info!"))
        );
    }

}
