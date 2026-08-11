package my.academy.springbookstore.repository;

import java.util.List;
import java.util.Optional;
import my.academy.springbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);

}
