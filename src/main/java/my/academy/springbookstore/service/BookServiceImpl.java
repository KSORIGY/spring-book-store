package my.academy.springbookstore.service;

import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.BookDto;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.dto.CreateBookRequestDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.BookMapper;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.book.BookRepository;
import my.academy.springbookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        return bookMapper.toDto(bookRepository
                .save(bookMapper.toModel(createBookRequestDto)));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can`t find book by id: " + id)));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto createBookRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find book by id: " + id)
        );
        bookMapper.updateBookFromBookDto(createBookRequestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        Specification<Book> bookSpecification =
                bookSpecificationBuilder.build(bookSearchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }
}
