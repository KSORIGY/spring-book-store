package my.academy.springbookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import my.academy.springbookstore.model.Status;

public record UpdateOrderRequestDto(
        @NotNull
        Status status
) {
}
