package my.academy.springbookstore.service.shoppingcart;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemUpdateDto;
import my.academy.springbookstore.dto.shoppingcart.ShoppingCartDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.CartItemMapper;
import my.academy.springbookstore.mapper.ShoppingCartMapper;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.model.CartItem;
import my.academy.springbookstore.model.ShoppingCart;
import my.academy.springbookstore.repository.book.BookRepository;
import my.academy.springbookstore.repository.cartitem.CartItemRepository;
import my.academy.springbookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart by id:" + userId))
        );
    }

    @Override
    public ShoppingCartDto addBookToCart(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart by id:" + userId)
        );

        Book book = bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find book by id:"
                        + cartItemRequestDto.bookId()));

        Optional<CartItem> isExsistsCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();

        if (isExsistsCartItem.isPresent()) {
            CartItem cartItem = isExsistsCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.quantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = cartItemMapper.toModel(cartItemRequestDto);
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setBook(book);
            cartItemRepository.save(newCartItem);
            shoppingCart.getCartItems().add(newCartItem);
        }
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItemQuantity(Long userId,
                                                  Long cartItemId,
                                                  CartItemUpdateDto cartItemUpdateDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart by id:" + userId)
        );

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cart item by id:" + cartItemId)
        );

        cartItem.setQuantity(cartItemUpdateDto.quantity());

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart by id:" + userId)
        );

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find cart item by id: " + cartItemId + " in your cart")
                );
        cartItemRepository.delete(cartItem);
    }
}
