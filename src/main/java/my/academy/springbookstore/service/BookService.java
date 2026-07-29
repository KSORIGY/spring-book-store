package my.academy.springbookstore.service;

import my.academy.springbookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
