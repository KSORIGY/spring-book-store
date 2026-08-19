package my.academy.springbookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.repository.SpecificationProvider;
import my.academy.springbookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements
        SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can`t find specification for: " + key));
    }
}
