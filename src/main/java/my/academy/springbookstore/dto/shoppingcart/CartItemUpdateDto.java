package my.academy.springbookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(
        @Positive
        int quantity
) {
}
