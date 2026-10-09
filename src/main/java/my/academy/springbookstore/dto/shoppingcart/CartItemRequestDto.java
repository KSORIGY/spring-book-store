package my.academy.springbookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull
        Long bookId,
        @Min(value = 1, message = "Quantity can`t be less than 1")
        int quantity
) {
}
