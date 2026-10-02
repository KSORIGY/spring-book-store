package my.academy.springbookstore.mapper;

import java.util.stream.Collectors;
import my.academy.springbookstore.dto.book.BookDto;
import my.academy.springbookstore.dto.book.BookDtoWithoutCategoryIds;
import my.academy.springbookstore.dto.book.CreateBookRequestDto;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateBookFromBookDto(CreateBookRequestDto createBookRequestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .toList());
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book,
                               CreateBookRequestDto createBookRequestDto) {
        if (createBookRequestDto.getCategoryIds() != null) {
            book.setCategories(createBookRequestDto.getCategoryIds().stream()
                    .map(id -> {
                        Category category = new Category();
                        category.setId(id);
                        return category;
                    })
                    .collect(Collectors.toSet()));
        }
    }
}

