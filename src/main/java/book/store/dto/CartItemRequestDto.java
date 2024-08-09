package book.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Positive
    @NotNull
    private Long bookId;
    @Positive(message = "Quantity must be more than 0!")
    private int quantity;
}
