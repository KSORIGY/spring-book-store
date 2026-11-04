package my.academy.springbookstore.service.shoppingcart;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import my.academy.springbookstore.dto.shoppingcart.CartItemDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemUpdateDto;
import my.academy.springbookstore.dto.shoppingcart.ShoppingCartDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.mapper.CartItemMapper;
import my.academy.springbookstore.mapper.ShoppingCartMapper;
import my.academy.springbookstore.model.Book;
import my.academy.springbookstore.model.CartItem;
import my.academy.springbookstore.model.ShoppingCart;
import my.academy.springbookstore.model.User;
import my.academy.springbookstore.repository.book.BookRepository;
import my.academy.springbookstore.repository.cartitem.CartItemRepository;
import my.academy.springbookstore.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private ShoppingCart shoppingCart;
    private Book book;
    private CartItem cartItem;
    private ShoppingCartDto shoppingCartDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setPrice(java.math.BigDecimal.TEN);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        CartItemDto cartItemDto = new CartItemDto(1L, 1L, "Test Book", 2);
        shoppingCartDto = new ShoppingCartDto(1L, 1L, Set.of(cartItemDto));
    }

    @Test
    @DisplayName("Get shopping cart by valid user ID")
    void getShoppingCart_ValidUserId_ReturnsShoppingCartDto() {
        Long userId = user.getId();

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(userId);

        assertEquals(shoppingCartDto, actual);

        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Throw exception when getting shopping cart by invalid user ID")
    void getShoppingCart_InvalidUserId_ThrowsException() {
        Long invalidUserId = 99L;

        when(shoppingCartRepository.findByUserId(invalidUserId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(invalidUserId));

        String expectedMessage = "Can`t find shopping cart by id:" + invalidUserId;
        assertEquals(expectedMessage, exception.getMessage());

        verify(shoppingCartRepository).findByUserId(invalidUserId);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Add a NEW book to shopping cart")
    void addBookToCart_NewBook_AddsItemAndReturnsDto() {
        Long userId = user.getId();
        CartItemRequestDto requestDto = new CartItemRequestDto(book.getId(), 3);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addBookToCart(userId, requestDto);

        assertEquals(shoppingCartDto, actual);

        verify(shoppingCartRepository).findByUserId(userId);
        verify(bookRepository).findById(requestDto.bookId());
        verify(cartItemMapper).toModel(requestDto);
        verify(cartItemRepository).save(cartItem);
        verify(shoppingCartMapper).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Add an EXISTING book to shopping cart (updates quantity)")
    void addBookToCart_ExistingBook_UpdatesQuantityAndReturnsDto() {
        Long userId = user.getId();
        shoppingCart.getCartItems().add(cartItem);
        int quantityToAdd = 3;
        int expectedQuantity = cartItem.getQuantity() + quantityToAdd;
        CartItemRequestDto requestDto = new CartItemRequestDto(book.getId(), quantityToAdd);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addBookToCart(userId, requestDto);

        assertEquals(shoppingCartDto, actual);
        assertEquals(expectedQuantity, cartItem.getQuantity());

        verify(shoppingCartRepository).findByUserId(userId);
        verify(bookRepository).findById(requestDto.bookId());
        verify(cartItemRepository).save(cartItem);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("Update cart item quantity")
    void updateCartItemQuantity_ValidData_UpdatesAndReturnsDto() {
        Long userId = user.getId();
        Long cartItemId = cartItem.getId();
        CartItemUpdateDto updateDto = new CartItemUpdateDto(10);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.updateCartItemQuantity(userId, cartItemId, updateDto);

        assertEquals(shoppingCartDto, actual);
        assertEquals(10, cartItem.getQuantity());

        verify(shoppingCartRepository).findByUserId(userId);
        verify(cartItemRepository).findByIdAndShoppingCartId(cartItemId, userId);
        verify(shoppingCartMapper).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Delete cart item")
    void deleteCartItem_ValidIds_Success() {
        Long userId = user.getId();
        Long cartItemId = cartItem.getId();

        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));

        shoppingCartService.deleteCartItem(userId, cartItemId);

        verify(cartItemRepository).findByIdAndShoppingCartId(cartItemId, userId);
        verify(cartItemRepository).delete(cartItem);
        verifyNoMoreInteractions(cartItemRepository, shoppingCartRepository);
    }

    @Test
    @DisplayName("Create empty shopping cart for user")
    void createShoppingCart_ValidUser_SavesCart() {
        shoppingCartService.createShoppingCart(user);

        verify(shoppingCartRepository).save(any(ShoppingCart.class));
        verifyNoMoreInteractions(shoppingCartRepository);
    }
}