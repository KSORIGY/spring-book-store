package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.CreateBookRequestDto;
import my.academy.springbookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);
}

