package my.academy.springbookstore.repository;

import java.util.List;
import my.academy.springbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
