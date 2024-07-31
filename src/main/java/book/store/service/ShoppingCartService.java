package book.store.service;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.model.CartItem;
import book.store.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    void createCartItem(CartItemRequestDto cartItemRequestDto,
                        Authentication authentication);

    ShoppingCartResponseDto getShoppingCart(Authentication authentication);

    void deleteCartItem(Long id,Authentication authentication);

    void updateCartItem(Long id, CartItemRequestDto cartItemRequestDto,
                        Authentication authentication);

    boolean checkCartItemOwnerMatching(Long id, Authentication authentication);

    CartItem toCartItem(CartItemRequestDto cartItemRequestDto, User user);
}
