package book.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank(message = "Category name can't be empty")
    private String name;
    @NotBlank(message = "Insert some description for this category!")
    private String description;
}
