package my.academy.springbookstore.service.order;

import my.academy.springbookstore.dto.order.OrderDto;
import my.academy.springbookstore.dto.order.OrderItemDto;
import my.academy.springbookstore.dto.order.OrderRequestDto;
import my.academy.springbookstore.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(Long userId, OrderRequestDto orderRequestDto);

    List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderRequestDto);

    List<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId);

}
