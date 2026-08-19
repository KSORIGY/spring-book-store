package my.academy.springbookstore.repository.book;

import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.SpecificationBuilder;
import my.academy.springbookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParams) {
        Specification<Book> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction();;

        if (searchParams.titles() != null && searchParams.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParams.titles()));
        }
        if (searchParams.authors() != null && searchParams.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParams.authors()));
        }
        if (searchParams.isbns() != null && searchParams.isbns().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(searchParams.isbns()));
        }

        return specification;
    }
}
