package my.academy.springbookstore.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    public static final String PRICE_KEY = "price";

    @Override
    public String getKey() {
        return PRICE_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params[0] != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                        new BigDecimal(params[0])));
            }

            if (params[1] != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                        new BigDecimal(params[1])));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
