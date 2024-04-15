package fei.stu.billing.infra.product.mapper;

import fei.stu.billing.domain.Product;
import fei.stu.billing.infra.product.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    Product mapFromEntity(ProductEntity entity);
}
