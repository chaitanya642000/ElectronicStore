package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateOrderRequestDto {

    @NotBlank(message = "User Id is required")
    private String userId;

    @NotBlank(message = "Cart Id is required")
    private String cartId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";

    @NotBlank(message = "Billing Address is required")
    private String billingAddress;

    @NotBlank(message = "Billing Phone cannot be blank")
    private String billingPhone;

    @NotBlank(message = "Billing Name cannot be blank")
    private String billingName;

}
