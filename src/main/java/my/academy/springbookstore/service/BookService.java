package my.academy.springbookstore.service;

import java.util.List;
import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
