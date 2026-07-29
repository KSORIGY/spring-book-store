package my.academy.springbookstore.repository;

import my.academy.springbookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
