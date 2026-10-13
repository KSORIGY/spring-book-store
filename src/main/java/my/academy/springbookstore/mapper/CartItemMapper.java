package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.shoppingcart.CartItemDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book")
    CartItem toModel(CartItemRequestDto cartItemRequestDto);

    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }

        Book book = new Book();
        book.setId(id);
        return book;
    }
}
