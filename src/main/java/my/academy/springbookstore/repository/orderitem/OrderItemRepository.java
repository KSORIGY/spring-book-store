package my.academy.springbookstore.repository.orderitem;

import java.util.Optional;
import my.academy.springbookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
