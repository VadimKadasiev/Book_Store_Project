package book.store.service.impl;

import book.store.dto.CartItemRequestDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.mapper.ShoppingCartMapper;
import book.store.model.Book;
import book.store.model.CartItem;
import book.store.model.ShoppingCart;
import book.store.model.User;
import book.store.repository.BookRepository;
import book.store.repository.CartItemRepository;
import book.store.repository.ShoppingCartRepository;
import book.store.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
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
    public ShoppingCartResponseDto createCartItem(CartItemRequestDto cartItemRequestDto,
                                                  Long userId) {
        Book book = bookRepository.getBookById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by Id: " + cartItemRequestDto.getBookId()));
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(userId).orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user Id " + userId));
        Optional<CartItem> cartItemFromDb = shoppingCart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getBook()
                        .getId()
                        .equals(book.getId()))
                .findAny();
        CartItem cartItem = new CartItem();
        if (cartItemFromDb.isPresent()) {
            cartItem = cartItemFromDb.get();
            cartItem.setQuantity(cartItemFromDb.get().getQuantity()
                    + cartItemRequestDto.getQuantity());
        } else {
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
            shoppingCart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(userId).orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by userId: " + userId));
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long id, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shoppingCart by user Id" + userId));
        cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new RuntimeException(
                        "User is not owned by this shopping cart."));
        System.out.println("deleted shopping cart item1");
        cartItemRepository.deleteById(id);
        System.out.println("deleted shopping cart item2");
        return shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto,
                                                  Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shoppingCart by user Id: " + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new RuntimeException(
                        "User is not owned by this shopping cart."));
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
    }
}
