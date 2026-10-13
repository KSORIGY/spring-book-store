package my.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.order.OrderDto;
import my.academy.springbookstore.dto.order.OrderItemDto;
import my.academy.springbookstore.dto.order.OrderRequestDto;
import my.academy.springbookstore.dto.order.UpdateOrderRequestDto;
import my.academy.springbookstore.model.User;
import my.academy.springbookstore.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order", description = "Place a new order")
    public OrderDto placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {
        return orderService.placeOrder(user.getId(), orderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get order history", description = "Show order history")
    public Page<OrderDto> getOrderHistory(@AuthenticationPrincipal User user,
                                          Pageable pageable) {
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status",
            description = "Update the status of an existing order (Admin only)")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items",
            description = "Show all OrderItems for a specific order")
    public List<OrderItemDto> getOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return orderService.getOrderItems(orderId, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get specific order item",
            description = "Show a specific OrderItem within an order")
    public OrderItemDto getOrderItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId, user.getId());
    }
}
