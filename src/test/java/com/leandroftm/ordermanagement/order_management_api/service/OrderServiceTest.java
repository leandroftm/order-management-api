package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateOrderItemRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateOrderRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.*;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.repository.OrderRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private final long id = 10L;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        List<CreateOrderItemRequest> orderItemRequest = new ArrayList<>();
        orderItemRequest.add(new CreateOrderItemRequest(1, 1L));

        CreateOrderRequest orderRequest = new CreateOrderRequest(
                orderItemRequest
        );

        Product savedProduct = new Product(
                "Product test",
                "",
                new BigDecimal("100.00"),
                100
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(savedProduct);
        orderItem.setUnitPrice(savedProduct.getPrice());
        orderItem.setQuantity(orderItemRequest.get(0).quantity());

        User savedUser = new User(
                "test@test.com",
                "123456",
                Role.USER
        );
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        Order savedOrder = new Order();
        //ReflectionUtils replacing this part -> Order.create(savedUser, orderItems);
        ReflectionTestUtils.setField(savedOrder, "id", 1L);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        long savedOrderId = orderService.createOrder(1L, orderRequest);

        assertEquals(savedOrderId, savedOrder.getId());
        assertEquals(99, savedProduct.getStock());

        verify(productRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(orderRepository).save(argThat(
                order -> order.getUser().getEmail().equals(savedUser.getEmail()) &&
                        order.getOrderItems().get(0).getProduct().getName().equals(savedProduct.getName())
        ));
        verifyNoMoreInteractions(orderRepository, productRepository, userRepository);
    }

    @Test
    void shouldReturnAllOrdersSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);
        List<Order> orders = new ArrayList<>();
        Order order = createOrder();
        orders.add(order);
        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<OrderResponse> responses = orderService.getAllOrders(null, pageable);

        assertNotNull(responses);
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());
        verify(orderRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnAllOrdersByStatusCreatedSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);
        List<Order> orders = new ArrayList<>();
        Order order = createOrder();
        orders.add(order);
        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(page);

        Page<OrderResponse> responses = orderService.getAllOrders(Status.CREATED, pageable);

        assertNotNull(responses);
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());
        verify(orderRepository).findByStatus(any(Status.class), any(Pageable.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnAllOrdersByStatusPaidSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);
        List<Order> orders = new ArrayList<>();
        Order order = createOrder();
        order.markAsPaid();
        orders.add(order);
        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(page);

        Page<OrderResponse> responses = orderService.getAllOrders(Status.PAID, pageable);

        assertNotNull(responses);
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());
        verify(orderRepository).findByStatus(any(Status.class), any(Pageable.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnAllOrdersByStatusCanceledSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);

        List<Order> orders = new ArrayList<>();

        Order order = createOrder();
        order.cancel();
        orders.add(order);

        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(page);

        Page<OrderResponse> responses = orderService.getAllOrders(Status.CANCELLED, pageable);

        assertNotNull(responses);
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());
        verify(orderRepository).findByStatus(any(Status.class), any(Pageable.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrdersByUserAndStatusSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);

        List<Order> orders = new ArrayList<>();

        Order order = createOrder();
        ReflectionTestUtils.setField(order.getUser(), "id", 1L);
        order.markAsPaid();
        orders.add(order);

        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findByUserIdAndStatus(1L, Status.PAID, pageable)).thenReturn(page);

        Page<OrderResponse> responses = orderService.getOrdersByUserAndOrderStatus(1L, Status.PAID, pageable);

        assertNotNull(responses);
        assertEquals(1L, responses.getContent().get(0).userId());
        assertEquals(Status.PAID, responses.getContent().get(0).status());
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());

        verify(orderRepository).findByUserIdAndStatus(1L, Status.PAID, pageable);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrdersByUserSuccessfully() {
        Pageable pageable = PageRequest.of(1, 10);

        List<Order> orders = new ArrayList<>();

        Order order = createOrder();
        ReflectionTestUtils.setField(order.getUser(), "id", 1L);
        orders.add(order);

        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findByUserId(1L,pageable)).thenReturn(page);

        Page<OrderResponse> responses = orderService.getOrdersByUser(1L, pageable);

        assertNotNull(responses);
        assertEquals(1L, responses.getContent().get(0).userId());
        assertEquals("Product Test", responses.getContent().get(0).orderItems().get(0).productName());
        assertEquals("Category Test", responses.getContent().get(0).orderItems().get(0).categoryName());

        verify(orderRepository).findByUserId(1L, pageable);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrderSuccessfully() {
        Order order = createOrder();
        ReflectionTestUtils.setField(order, "id", id);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrder(id);

        assertEquals(id, response.id());
        assertEquals("Product Test", response.orderItems().get(0).productName());
        assertEquals("Category Test", response.orderItems().get(0).categoryName());

        verify(orderRepository).findById(id);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrderByIdAndUserIdSuccessfully() {
        List<OrderItem> orderItems = getOrderItems();

        User savedUser = new User("User Test", "123456", Role.ADMIN);
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        Order savedOrder = Order.create(savedUser, orderItems);
        ReflectionTestUtils.setField(savedOrder, "id", id);

        when(orderRepository.findByIdAndUserId(id, 1L)).thenReturn(Optional.of(savedOrder));

        OrderResponse response = orderService.findByIdAndUserId(id, 1L);

        assertEquals(1L, response.userId());
        assertEquals(id, response.id());
        assertEquals("Product Test", response.orderItems().get(0).productName());
        assertEquals("Category Test", response.orderItems().get(0).categoryName());

        verify(orderRepository).findByIdAndUserId(id, 1L);
        verifyNoMoreInteractions(orderRepository);
    }

    //TODO EXCEPTIONS

    @Test
    void shouldPayOrderSuccessfully(){
        Order savedOrder = createOrder();
        ReflectionTestUtils.setField(savedOrder, "id", id);

        when(orderRepository.findById(id)).thenReturn(Optional.of(savedOrder));

        orderService.payOrder(id);

        verify(orderRepository).findById(id);
        verify(orderRepository).save(argThat(order ->
                order.getStatus().equals(Status.PAID)));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldCancelOrderSuccessfully(){
        Order savedOrder = createOrder();
        ReflectionTestUtils.setField(savedOrder, "id", id);

        when(orderRepository.findById(id)).thenReturn(Optional.of(savedOrder));
        orderService.cancelOrder(id);

        verify(orderRepository).findById(id);
        verify(orderRepository).save(argThat(order ->
                order.getStatus().equals(Status.CANCELLED)));
        verifyNoMoreInteractions(orderRepository);
    }

    private static @NonNull List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = new Product(
                "Product Test",
                "",
                new BigDecimal("100.00"),
                100);
        Category category = new Category(
                "Category Test"
        );
        product.setCategory(category);
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItems.add(orderItem);
        return orderItems;
    }


    //helper
    public Order createOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = new Product(
                "Product Test",
                "",
                new BigDecimal("100.00"),
                100);
        Category category = new Category(
                "Category Test"
        );
        product.setCategory(category);
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItems.add(orderItem);

        return Order.create(
                new User("test@test.com", "123456", Role.USER),
                orderItems
        );
    }
}
