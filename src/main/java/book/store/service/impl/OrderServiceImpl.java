package book.store.service.impl;

import book.store.dto.CartItemResponseDto;
import book.store.dto.OrderItemResponseDto;
import book.store.dto.OrderRequestDto;
import book.store.dto.OrderResponseDto;
import book.store.dto.ShoppingCartResponseDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.OrderItemMapper;
import book.store.mapper.OrderMapper;
import book.store.model.Book;
import book.store.model.Order;
import book.store.model.OrderItem;
import book.store.model.User;
import book.store.repository.BookRepository;
import book.store.repository.CartItemRepository;
import book.store.repository.OrderItemRepository;
import book.store.repository.OrderRepository;
import book.store.service.OrderService;
import book.store.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartService shoppingCartService;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(User user, OrderRequestDto orderRequestDto) {
        if (shoppingCartService.getShoppingCart(user.getId()).getCartItems().isEmpty()) {
            throw new RuntimeException("Shopping Cart is empty");
        }
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setUser(user);
        orderRepository.save(order);
        createOrderItems(user, order);
        order.setOrderItems(orderItemRepository.findOrderItemByOrderId(order.getId()));
        return orderMapper.orderToOrderResponseDto(order);
    }

    @Transactional
    public void createOrderItems(User user, Order order) {
        ShoppingCartResponseDto shoppingCartResponseDto =
                shoppingCartService.getShoppingCart(user.getId());
        Set<CartItemResponseDto> cartItemResponseDtos = shoppingCartResponseDto.getCartItems();
        for (CartItemResponseDto cartItemResponseDto : cartItemResponseDtos) {
            Book book = bookRepository.getBookById(cartItemResponseDto.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find book by id "
                            + cartItemResponseDto.getBookId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setPrice(book.getPrice());
            orderItem.setQuantity(cartItemResponseDto.getQuantity());
            orderItem.setOrder(order);
            order.setTotal(order.getTotal()
                    .add(book.getPrice()
                            .multiply(BigDecimal
                                    .valueOf(cartItemResponseDto.getQuantity()))));
            orderItemRepository.save(orderItem);
            cartItemRepository.deleteById(cartItemResponseDto.getId());
        }
    }

    @Override
    public List<OrderResponseDto> getOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId()).stream().map(
                orderMapper::orderToOrderResponseDto).toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, long orderItemId, User user) {
        checkUserOrderMatching(user, orderId);
        return orderItemMapper.toOrderItemResponseDto(orderItemRepository
                .findOrderItemByOrderIdAndId(orderId, orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item by id "
                        + orderItemId + " in order by ID " + orderId)));
    }

    @Override
    public Set<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable) {
        checkUserOrderMatching(user, orderId);
        return orderItemRepository.findOrderItemByOrderId(orderId).stream()
                .map(orderItemMapper::toOrderItemResponseDto).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void changeOrderStatus(Long orderId, Order.OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    private void checkUserOrderMatching(User user, Long orderId) {
        if (orderRepository.findByIdAndUserId(orderId, user.getId()) == null) {
            throw new RuntimeException("This user has no order with id " + orderId);
        }
    }
}
