package my.academy.springbookstore.service.shoppingcart;

import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemUpdateDto;
import my.academy.springbookstore.dto.shoppingcart.ShoppingCartDto;
import my.academy.springbookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addBookToCart(Long userId,
                                  CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateCartItemQuantity(Long userId,
                                           Long cartItemId,
                                           CartItemUpdateDto cartItemUpdateDto);

    void deleteCartItem(Long userId, Long cartItemId);

    void createShoppingCart(User user);
}
