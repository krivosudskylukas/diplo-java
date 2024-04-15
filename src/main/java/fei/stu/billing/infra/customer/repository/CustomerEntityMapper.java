package fei.stu.billing.infra.customer.repository;

import fei.stu.billing.domain.Invoice;
import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    Invoice mapFromEntity(InvoiceEntity entity);
}
