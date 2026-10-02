package my.academy.springbookstore.repository.category;

import my.academy.springbookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
