package my.academy.springbookstore.mapper;

import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.CreateBookRequestDto;
import my.academy.springbookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateBookFromBookDto(CreateBookRequestDto createBookRequestDto, @MappingTarget Book book);
}

