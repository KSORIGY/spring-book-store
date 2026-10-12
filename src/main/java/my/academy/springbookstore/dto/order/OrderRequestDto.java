package my.academy.springbookstore.dto.order;

import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull
        String shippingAddress
) {
}
