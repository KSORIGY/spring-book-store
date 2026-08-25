package my.academy.springbookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title can not be null or empty")
    private String title;
    @NotBlank(message = "Author can not be null or empty")
    private String author;
    @NotBlank(message = "ISBN can not be null or empty")
    private String isbn;
    @NotNull(message = "Price can not be null")
    @Min(value = 0, message = "Price can not be less than 0")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
