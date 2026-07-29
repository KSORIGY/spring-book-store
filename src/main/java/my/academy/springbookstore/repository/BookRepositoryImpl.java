package my.academy.springbookstore.repository;

import jakarta.persistence.EntityManager;
import my.academy.springbookstore.model.Book;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private EntityManager entityManager;

    public BookRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }
}
