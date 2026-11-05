package my.academy.springbookstore.repository.shoppingcart;

import my.academy.springbookstore.model.ShoppingCart;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by user id")
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart-with-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/remove-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidUserId_ReturnsShoppingCart() {
        Long expectedUserId = 100L;
        int expectedCartItemSise = 1;
        int expectedQuantity = 10;
        String expectedBookTitle = "Test Cart Book";

        Optional<ShoppingCart> actualShoppingCart = shoppingCartRepository.findByUserId(expectedUserId);

        assertTrue(actualShoppingCart.isPresent());

        ShoppingCart actualCart = actualShoppingCart.get();

        assertEquals(expectedUserId, actualCart.getUser().getId());
        assertEquals(expectedCartItemSise, actualCart.getCartItems().size());
        assertEquals(expectedBookTitle, actualCart.getCartItems().iterator().next().getBook().getTitle());
        assertEquals(expectedQuantity, actualCart.getCartItems().iterator().next().getQuantity());

    }


}