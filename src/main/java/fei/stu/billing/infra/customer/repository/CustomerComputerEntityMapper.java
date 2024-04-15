package fei.stu.billing.infra.customer.repository;

import fei.stu.billing.domain.CustomerComputer;
import fei.stu.billing.infra.customer.entity.CustomerComputerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerComputerEntityMapper {

    CustomerComputer mapFromEntity(CustomerComputerEntity entity);
}
