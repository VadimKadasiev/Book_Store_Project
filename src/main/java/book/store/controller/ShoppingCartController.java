package book.store.controller;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public void createCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        shoppingCartService.createCartItem(cartItemRequestDto,authentication);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto getShoppingCartResponseDto(Authentication authentication) {
        return shoppingCartService.getShoppingCart(authentication);
    }

    @DeleteMapping("items/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItem(@PathVariable Long id, Authentication authentication) {
        shoppingCartService.deleteCartItem(id,authentication);
    }

    @PutMapping("items/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void updateCartItem(@PathVariable Long id,
                               @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
                               Authentication authentication) {
        shoppingCartService.updateCartItem(id,cartItemRequestDto,authentication);
    }
}