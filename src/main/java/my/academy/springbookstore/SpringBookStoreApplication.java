package my.academy.springbookstore;

import java.math.BigDecimal;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("title");
            book.setAuthor("author");
            book.setDescription("hello");
            book.setIsbn("1234");
            book.setPrice(BigDecimal.valueOf(299.99));

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }

}
