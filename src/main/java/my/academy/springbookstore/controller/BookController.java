package my.academy.springbookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.dto.CreateBookRequestDto;
import my.academy.springbookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public Page<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto save(@RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.save(createBookRequestDto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.update(id, createBookRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    public Page<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        return bookService.search(bookSearchParameters, pageable);
    }
}
