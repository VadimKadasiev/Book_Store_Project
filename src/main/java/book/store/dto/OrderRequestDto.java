package book.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank
    private String shippingAddress;
}
