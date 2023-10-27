package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.CartItemDto;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto>addItemToCart(@PathVariable String userId ,@RequestBody AddItemToCartRequest
                                                additem)

    {
        CartDto cartDto = cartService.addItemToCart(userId,additem);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    //delete items from cart
    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<ApiMessageResponse>deleteItemfFromCart(@PathVariable String userId,
                                                                 @PathVariable int itemId)

    {
        cartService.removeItemFromCart(userId,itemId);
        ApiMessageResponse message = ApiMessageResponse.builder()
                .message("Item removed from cart successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    //clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiMessageResponse>clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiMessageResponse message = ApiMessageResponse.builder()
                .message("Cart Cleared successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto>getCart(@PathVariable String userId)
    {
        CartDto cartDto = cartService.getCart(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }
}
