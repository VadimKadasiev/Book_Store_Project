package book.store.controller;

import book.store.dto.OrderItemResponseDto;
import book.store.dto.OrderRequestDto;
import book.store.dto.OrderResponseDto;
import book.store.model.Order;
import book.store.model.User;
import book.store.service.OrderService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(getCurrentUser(authentication), orderRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponseDto> getOrders(Authentication authentication, Pageable pageable) {
        return orderService.getOrders(getCurrentUser(authentication), pageable);
    }

    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getOrderItem(Authentication authentication,
                                             @PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId, getCurrentUser(authentication));
    }

    @GetMapping({"{id}/items"})
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemResponseDto> getOrderItems(@PathVariable Long id,
                                                   Authentication authentication,
                                                   Pageable pageable) {
        return orderService.getOrderItems(id, getCurrentUser(authentication),pageable);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeOrderStatus(@PathVariable Long id, @RequestBody String status) {
        status = status.replace("\"", "")
                .replace("}", "")
                .trim()
                .substring(15);
        orderService.changeOrderStatus(id, Order.OrderStatus.valueOf(status));
    }

    private User getCurrentUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
