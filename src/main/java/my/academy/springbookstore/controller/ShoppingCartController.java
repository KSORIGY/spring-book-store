package my.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemUpdateDto;
import my.academy.springbookstore.dto.shoppingcart.ShoppingCartDto;
import my.academy.springbookstore.model.User;
import my.academy.springbookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get users` shopping cart",
            description = "Get authenticated user's shopping cart")
    public ShoppingCartDto getShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book to cart",
            description = "Add a new book or increase quantity of an existing in the cart")
    public ShoppingCartDto addBookToCart(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity",
            description = "Update the quantity of a book in the shopping cart")
    public ShoppingCartDto updateCartItemQuantity(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto updateDto) {
        return shoppingCartService.updateCartItemQuantity(user.getId(), cartItemId, updateDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove book from cart",
            description = "Delete an item from the shopping cart")
    public void deleteCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(user.getId(), cartItemId);
    }

}
