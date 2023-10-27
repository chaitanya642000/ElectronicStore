package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CreateOrderRequestDto;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto orderDto);
    void removeOrder(String orderId);
    List<OrderDto>getAllOrdersOfUsers(String userId);

    PageableResponse<OrderDto>getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);

    public OrderDto updateOrder(String orderId);


}
