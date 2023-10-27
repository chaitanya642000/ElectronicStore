package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.Product;
import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartitem;
    private ProductDto product;
    private int quantity;
    private int totalPrice;
}
