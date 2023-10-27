package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import com.lcwd.electronic.store.dtos.CreateOrderRequestDto;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //create order
    @PostMapping("/createorder")
    public ResponseEntity<OrderDto>createOrder(@RequestBody @Valid CreateOrderRequestDto orderRequestDto)
    {
       OrderDto orderDto1 = orderService.createOrder(orderRequestDto);
       return new ResponseEntity<>(orderDto1, HttpStatus.CREATED);
    }

    //remove order
    @DeleteMapping("/deleteorder/{orderId}")
    public ApiMessageResponse deleteOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiMessageResponse response = ApiMessageResponse.builder()
                .success(true)
                .message("Order with given Id successfully deleted")
                .status(HttpStatus.OK).build();

        return response;
    }

    //get list of orders of only user
    @GetMapping("/getOrders/{userId}")
    public ResponseEntity<List<OrderDto>>getAllOrdersOfUser(@PathVariable String userId)
    {
        List<OrderDto>listOfOrders = orderService.getAllOrdersOfUsers(userId);
        return new ResponseEntity<>(listOfOrders,HttpStatus.OK);
    }

    //get orders of all users
    @GetMapping("/getAllOrdersOfUsers")
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
                                                                   @RequestParam(value = "pageNumber",defaultValue = "0") Integer pageNumber,
                                                                   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(value = "sortDir",defaultValue = "asc") String sortDir,
                                                                   @RequestParam(value = "sortBy",defaultValue = "orderedDate") String sortBy
                                                                   )
    {
        //int pageNumber, int pageSize, String sortBy, String sortDir
        PageableResponse<OrderDto>pageableResponse = orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto>updateOrderStatus(@PathVariable String orderId)
    {
        OrderDto order = orderService.updateOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
