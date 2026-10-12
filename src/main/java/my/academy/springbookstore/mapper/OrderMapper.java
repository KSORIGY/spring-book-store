package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.order.OrderDto;
import my.academy.springbookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

}
