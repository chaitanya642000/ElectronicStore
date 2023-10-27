package com.lcwd.electronic.store.dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AddItemToCartRequest {

    private String productId;
    private int quantity;
}
