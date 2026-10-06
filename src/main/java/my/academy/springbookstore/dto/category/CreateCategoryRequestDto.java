package my.academy.springbookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank(message = "Category name can not be null or empty")
    private String name;
    private String description;
}
