package fei.stu.billing.infra.customer.repository;

import fei.stu.billing.domain.Customer;
import fei.stu.billing.infra.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    Customer mapFromEntity(CustomerEntity entity);
}
