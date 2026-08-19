package my.academy.springbookstore.repository.book.spec;

import java.util.Arrays;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "isbn";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get("isbn").in(Arrays.stream(params).toArray()));
    }
}
