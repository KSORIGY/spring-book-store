package my.academy.springbookstore.service;

import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);
}
