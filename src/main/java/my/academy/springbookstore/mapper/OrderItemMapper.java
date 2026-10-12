package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.order.OrderItemDto;
import my.academy.springbookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

}
