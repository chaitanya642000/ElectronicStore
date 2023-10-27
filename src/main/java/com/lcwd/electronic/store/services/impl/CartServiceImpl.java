package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.controllers.CartController;
import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements CartService {

    private Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Method to add items to cart
     *
     * @param userId
     * @param request
     * @return CartDto
     */
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        Product product =  productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given Id does not found"));
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given Id does not found"));

        Cart cart = null;

        try
        {
            //get cart of the user
            cart =  cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException ex)
        {
            //if cart does not exists create new one
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //check if cartItem already exists
        List<CartItem> items = cart.getCartItems();

        AtomicReference<Boolean>cartItemUpdated = new AtomicReference<>(false);
        items = items.stream().map(cartItem -> {
            if(cartItem.getProduct().getProductId().equals(productId))
            {
                    cartItemUpdated.set(true);
                    cartItem.setQuantity(quantity);
                    cartItem.setTotalPrice(quantity*product.getPrice());
            }
            return cartItem;
        }).collect(Collectors.toList());
     //   cart.setCartItems(updatedCartItems);
        //create cartItem with current input parameter
        if(cartItemUpdated.get() == false)
        {
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(quantity*product.getDiscountedPrice())
                    .cart(cart)
                    .build();

            cart.getCartItems().add(cartItem);
        }

        //add item to the cart
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        CartDto cartDto =  mapper.map(updatedCart, CartDto.class);
        return cartDto;

    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem Cartitem = cartItemRepository.findById(cartItem).orElseThrow(()->{
            throw new ResourceNotFoundException("Cart with given Id does not found");
        });
        cartItemRepository.delete(Cartitem);
    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new ResourceNotFoundException("User with given Id does not exists");
        });

        Cart cart = cartRepository.findByUser(user).orElseThrow(()->{
            throw new ResourceNotFoundException("Cart with given user does not exits");
        });

        cart.getCartItems().clear();
        logger.info("Value of cart is"+cart);

        cartRepository.save(cart);
    }

    public CartDto getCart(String userId)
    {
        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new ResourceNotFoundException("User with given Id does not exists");
        });

        Cart cart = cartRepository.findByUser(user).orElseThrow(()->{
            throw new ResourceNotFoundException("Cart with given user does not exits");
        });

        CartDto cartDto =  mapper.map(cart,CartDto.class);
        return cartDto;
    }
}
