package book.store.service.impl;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.mapper.ShoppingCartMapper;
import book.store.model.CartItem;
import book.store.model.ShoppingCart;
import book.store.model.User;
import book.store.repository.BookRepository;
import book.store.repository.CartItemRepository;
import book.store.repository.ShoppingCartRepository;
import book.store.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void createCartItem(CartItemRequestDto cartItemRequestDto, User user) {
        CartItem cartItem = toCartItem(cartItemRequestDto, user);
        cartItemRepository.save(cartItem);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow();
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long id, User user) {
        if (!checkCartItemOwnerMatching(id, user)) {
            throw new RuntimeException("User is not owned by this shopping cart.");
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public void updateCartItem(Long id, CartItemRequestDto cartItemRequestDto, User user) {
        if (!checkCartItemOwnerMatching(id, user)) {
            throw new RuntimeException("User is not owned by this shopping cart.");
        }
        CartItem cartItem = toCartItem(cartItemRequestDto, user);
        cartItem.setId(id);
        cartItemRepository.save(cartItem);
    }

    public boolean checkCartItemOwnerMatching(Long id, User user) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        return cartItem.getShoppingCart().getUser().getId().equals(user.getId());
    }

    public CartItem toCartItem(CartItemRequestDto cartItemRequestDto, User user) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.getBookById(cartItemRequestDto.getBookId()).orElseThrow());
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow());
        return cartItem;
    }
}
