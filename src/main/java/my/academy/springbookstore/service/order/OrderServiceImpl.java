package my.academy.springbookstore.service.order;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            throw new IllegalStateException("Shopping cart is empty!");
        }

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());

            orderItems.add(orderItem);

            BigDecimal itemPrice = orderItem.getPrice()
                    .multiply(
                            BigDecimal.valueOf(orderItem.getQuantity())
                    );

            total = total.add(itemPrice);
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        cartItemRepository.deleteAll(shoppingCart.getCartItems());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return List.of();
    }

    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderRequestDto) {
        return null;
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Long userId) {
        return List.of();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId) {
        return null;
    }

    private Set<OrderItem> createOrderItem(ShoppingCart shoppingCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();

    }
}
