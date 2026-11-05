package my.academy.springbookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import my.academy.springbookstore.dto.shoppingcart.CartItemRequestDto;
import my.academy.springbookstore.dto.shoppingcart.CartItemUpdateDto;
import my.academy.springbookstore.dto.shoppingcart.ShoppingCartDto;
import my.academy.springbookstore.model.Role;
import my.academy.springbookstore.model.RoleName;
import my.academy.springbookstore.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(100L);
        user.setEmail("testuser@gmail.com");
        user.setPassword("password");

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        user.setRoles(Set.of(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Get shopping cart")
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/remove-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_ValidUser_ReturnsShoppingCartDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(100L, actual.userId());
        assertEquals(1, actual.cartItems().size());
    }

    @Test
    @DisplayName("Add book to cart")
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/remove-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBookToCart_ValidRequest_ReturnsUpdatedShoppingCartDto() throws Exception {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto(100L, 5);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(15, actual.cartItems().iterator().next().quantity());
    }

    @Test
    @DisplayName("Update cart item quantity")
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/remove-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCartItemQuantity_ValidRequest_ReturnsUpdatedShoppingCartDto() throws Exception {
        Long cartItemId = 100L;
        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto(20);
        String jsonRequest = objectMapper.writeValueAsString(cartItemUpdateDto);

        MvcResult result = mockMvc.perform(put("/cart/items/{cartItemId}", cartItemId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(20, actual.cartItems().iterator().next().quantity());
    }

    @Test
    @DisplayName("Delete cart item")
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/remove-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCartItem_ValidId_Success() throws Exception {
        Long cartItemId = 100L;

        mockMvc.perform(delete("/cart/items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}