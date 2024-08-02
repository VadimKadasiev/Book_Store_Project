package book.store.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Positive
    private Long bookId;
    @Positive(message = "Quantity must be more than null!")
    private Long quantity;
}
