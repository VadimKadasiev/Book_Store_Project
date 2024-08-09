package book.store.service;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto createCartItem(CartItemRequestDto cartItemRequestDto,
                                           Long userId);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto deleteCartItem(Long id, Long userId);

    ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto,
                                           Long userId);
}
