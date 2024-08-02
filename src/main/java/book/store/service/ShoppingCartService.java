package book.store.service;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.model.CartItem;
import book.store.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    void createCartItem(CartItemRequestDto cartItemRequestDto,
                        User user);

    ShoppingCartResponseDto getShoppingCart(User user);

    void deleteCartItem(Long id, User user);

    void updateCartItem(Long id, CartItemRequestDto cartItemRequestDto,
                        User user);

    boolean checkCartItemOwnerMatching(Long id, User user);

    CartItem toCartItem(CartItemRequestDto cartItemRequestDto, User user);
}
