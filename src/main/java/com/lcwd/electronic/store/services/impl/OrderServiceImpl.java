package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequestDto orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User fetchedUser = userRepository.findById(userId).orElseThrow(()->{
            throw new ResourceNotFoundException("User with given Id not found");
        });

        Cart cart = cartRepository.findById(cartId).orElseThrow(()->{
            throw new ResourceNotFoundException("User with given Id does not have cart");
        });

        List<CartItem>cartitems = cart.getCartItems();

        Order order = Order.builder()
                .orderedDate(new Date())
                .orderStatus(orderDto.getOrderStatus())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .billingName(orderDto.getBillingName())
                .paymentStatus(orderDto.getPaymentStatus())
                .deliveredDate(null)
                .orderId(UUID.randomUUID().toString())
                .user(fetchedUser)
                .build();

        //orderitems , amount remaing

        AtomicReference<Integer>totalOrderValue = new AtomicReference<>(0);

        List<OrderItem> orderItems = cartitems.stream().map(cartItem -> {
//            CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            totalOrderValue.set(totalOrderValue.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderAmount(totalOrderValue.get());
        order.setOrderItems(orderItems);

        cart.getCartItems().clear();

        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->{
            throw new ResourceNotFoundException("Order with given ID does not found");
        });
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new ResourceNotFoundException("User not found exception");
        });
        List<Order>orders = orderRepository.findByUser(user);
        List<OrderDto>orderDtos = orders.stream().map(order -> {
            return mapper.map(order,OrderDto.class);
        }).collect(Collectors.toList());

        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);

        PageableResponse<OrderDto>pageableResponse = Helper.getPageableResponse(page,OrderDto.class);
        return pageableResponse;
    }

    @Override
    public OrderDto updateOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->{
            throw new ResourceNotFoundException("Order with given Id does not exisits");
        });

        order.setDeliveredDate(new Date());
        order.setPaymentStatus("PAID");

        if(order.getPaymentStatus() == "PAID" && order.getDeliveredDate() != null)
        {
            order.setOrderStatus("DELEIVERED");
        }

        orderRepository.save(order);
        return mapper.map(order,OrderDto.class);
    }




}
