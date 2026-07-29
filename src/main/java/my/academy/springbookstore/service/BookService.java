package my.academy.springbookstore.service;

import java.util.List;
import my.academy.springbookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
