package my.academy.springbookstore.repository.book;

import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.BookSearchParameters;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.SpecificationBuilder;
import my.academy.springbookstore.repository.SpecificationProviderManager;
import my.academy.springbookstore.repository.book.spec.AuthorSpecificationProvider;
import my.academy.springbookstore.repository.book.spec.IsbnSpecificationProvider;
import my.academy.springbookstore.repository.book.spec.PriceSpecificationProvider;
import my.academy.springbookstore.repository.book.spec.TitleSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParams) {
        Specification<Book> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction();

        if (searchParams.titles() != null && searchParams.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE_KEY)
                    .getSpecification(searchParams.titles()));
        }
        if (searchParams.authors() != null && searchParams.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR_KEY)
                    .getSpecification(searchParams.authors()));
        }
        if (searchParams.isbns() != null && searchParams.isbns().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(IsbnSpecificationProvider.ISBN_KEY)
                    .getSpecification(searchParams.isbns()));
        }
        if (searchParams.priceFrom() != null || searchParams.priceTo() != null) {
            String[] priceParams = new String[2];

            priceParams[0] = searchParams.priceFrom() != null ? searchParams.priceFrom()
                    .toString() : null;

            priceParams[1] = searchParams.priceTo() != null ? searchParams.priceTo()
                    .toString() : null;

            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PriceSpecificationProvider.PRICE_KEY)
                    .getSpecification(priceParams));
        }

        return specification;
    }
}
