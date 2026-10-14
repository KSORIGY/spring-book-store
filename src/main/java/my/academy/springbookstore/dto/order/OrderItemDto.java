package my.academy.springbookstore.dto.order;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
