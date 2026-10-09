package my.academy.springbookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;

public record CartItemUpdateDto(
        @Min(value = 1)
        int quantity
) {
}
