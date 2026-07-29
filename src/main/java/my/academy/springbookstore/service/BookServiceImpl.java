package my.academy.springbookstore.service;

import java.util.List;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
