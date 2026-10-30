package my.academy.springbookstore.repository.book;

import my.academy.springbookstore.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by valid category ID")
    @Sql(scripts = "classpath:database/books/add-book-to-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-from-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByCategoriesId_ValidCategoryId_ReturnsBooksPage() {
        Long categoryId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Book> actualPage = bookRepository.findByCategoriesId(categoryId, pageRequest);

        assertEquals(1, actualPage.getTotalElements());
        assertEquals("Test Book", actualPage.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Find books by non-existing category ID returns empty page")
    @Sql(scripts = "classpath:database/books/add-book-to-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-from-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByCategoriesId_InvalidCategoryId_ReturnsEmptyPage() {
        Long nonExistingCategoryId = 100L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Book> actualPage = bookRepository.findByCategoriesId(nonExistingCategoryId, pageRequest);

        assertTrue(actualPage.isEmpty());
    }
}
