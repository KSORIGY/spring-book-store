package my.academy.springbookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import my.academy.springbookstore.model.Status;

public record UpdateOrderRequestDto(
        @NotBlank
        Status status
) {
}
