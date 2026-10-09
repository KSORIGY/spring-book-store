package my.academy.springbookstore.repository.cartitem;

import my.academy.springbookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
