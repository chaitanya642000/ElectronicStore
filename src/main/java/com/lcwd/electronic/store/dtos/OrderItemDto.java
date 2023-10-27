package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.Product;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private String orderItemId;
    private int quantity;
    private int totalPrice;
    private Product product;
}
