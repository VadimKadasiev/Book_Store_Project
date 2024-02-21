package book.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "Title field can't be empty")
    private String title;
    @NotNull(message = "Author field can't be empty")
    private String author;
    @NotNull(message = "ISBN field can't be empty")
    private String isbn;
    @NotNull(message = "Price field can't be empty")
    @Min(value = 0,message = "Price must have a positive value")
    private BigDecimal price;
    @NotNull(message = "Description field can't be empty")
    private String description;
    @NotNull(message = "Cover_Image field can't be empty")
    private String coverImage;
}
