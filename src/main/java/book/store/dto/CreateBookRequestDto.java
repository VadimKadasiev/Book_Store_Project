package book.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title field can't be empty")
    private String title;
    @NotBlank(message = "Author field can't be empty")
    private String author;
    @NotBlank(message = "ISBN field can't be empty")
    private String isbn;
    @NotNull(message = "Price field can't be empty")
    @Min(value = 0, message = "Price must have a positive value")
    private BigDecimal price;
    @NotBlank(message = "Description field can't be empty")
    private String description;
    @NotBlank(message = "Cover_Image field can't be empty")
    private String coverImage;
    @NotEmpty(message = "CategoryIds field can't be empty")
    private Set<Long> categoryIds;
}
