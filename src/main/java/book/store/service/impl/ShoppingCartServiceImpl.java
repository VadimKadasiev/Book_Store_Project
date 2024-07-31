package book.store.service.impl;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.mapper.CartItemMapper;
import book.store.mapper.ShoppingCartMapper;
import book.store.model.CartItem;
import book.store.model.ShoppingCart;
import book.store.model.User;
import book.store.repository.BookRepository;
import book.store.repository.CartItemRepository;
import book.store.repository.ShoppingCartRepository;
import book.store.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void createCartItem(CartItemRequestDto cartItemRequestDto,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = toCartItem(cartItemRequestDto,user);
        cartItemRepository.save(cartItem);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId())
                .orElseThrow();
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long id,Authentication authentication) {
        if (checkCartItemOwnerMatching(id,authentication)) {
            cartItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("User is not owned by this shopping cart.");
        }
    }

    @Override
    public void updateCartItem(Long id, CartItemRequestDto cartItemRequestDto,
                               Authentication authentication) {
        if (checkCartItemOwnerMatching(id,authentication)) {
            User user = (User) authentication.getPrincipal();
            CartItem cartItem = toCartItem(cartItemRequestDto,user);
            cartItem.setId(id);
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("User is not owned by this shopping cart.");
        }
    }

    public boolean checkCartItemOwnerMatching(Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        return cartItem.getShoppingCart().getUser().getId().equals(user.getId());
    }

    public CartItem toCartItem(CartItemRequestDto cartItemRequestDto,User user) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.getBookById(cartItemRequestDto.getBookId()).orElseThrow());
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow());
        return cartItem;
    }
}
