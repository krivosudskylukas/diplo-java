package fei.stu.billing.infra.invoice.mapper;

import fei.stu.billing.domain.Invoice;
import fei.stu.billing.infra.invoice.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceEntityMapper {

    Invoice mapFromEntity(InvoiceEntity entity);
}
