package my.academy.springbookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.order.OrderDto;
import my.academy.springbookstore.dto.order.OrderItemDto;
import my.academy.springbookstore.dto.order.OrderRequestDto;
import my.academy.springbookstore.dto.order.UpdateOrderRequestDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.OrderItemMapper;
import my.academy.springbookstore.mapper.OrderMapper;
import my.academy.springbookstore.model.CartItem;
import my.academy.springbookstore.model.Order;
import my.academy.springbookstore.model.OrderItem;
import my.academy.springbookstore.model.ShoppingCart;
import my.academy.springbookstore.model.Status;
import my.academy.springbookstore.repository.cartitem.CartItemRepository;
import my.academy.springbookstore.repository.order.OrderRepository;
import my.academy.springbookstore.repository.orderitem.OrderItemRepository;
import my.academy.springbookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto placeOrder(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find shopping cart by userId " + userId
                        )
                );

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException(
                    "Shopping cart is empty for user id: " + userId);
        }

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        Set<OrderItem> orderItems = createOrderItems(shoppingCart, order);
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        shoppingCart.clearCart();

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderRequestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find order by id: " + id)
        );

        order.setStatus(updateOrderRequestDto.status());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find order by id: " + orderId + " for user: " + userId)
        );

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.
                findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can`t find item: " + itemId + " in order: " + orderId
                        )
                );

        return orderItemMapper.toDto(orderItem);
    }

    private Set<OrderItem> createOrderItems(ShoppingCart shoppingCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            BigDecimal orderItemPrice = orderItem.getPrice().multiply(
                    BigDecimal.valueOf(orderItem.getQuantity())
            );
            total = total.add(orderItemPrice);
        }
        return total;
    }
}
