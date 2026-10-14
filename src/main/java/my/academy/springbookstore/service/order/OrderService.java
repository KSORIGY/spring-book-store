package my.academy.springbookstore.service.order;

import java.util.List;
import my.academy.springbookstore.dto.order.OrderDto;
import my.academy.springbookstore.dto.order.OrderItemDto;
import my.academy.springbookstore.dto.order.OrderRequestDto;
import my.academy.springbookstore.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userId, OrderRequestDto orderRequestDto);

    Page<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderRequestDto);

    List<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId);

}
