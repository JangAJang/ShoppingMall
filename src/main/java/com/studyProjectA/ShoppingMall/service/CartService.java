package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.*;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.*;
import com.studyProjectA.ShoppingMall.repository.CartItemRepository;
import com.studyProjectA.ShoppingMall.repository.CartRepository;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemDto> getMyCart(User user){
        Cart cart = cartRepository.findByBuyer(user).orElseThrow(CartNotFoundException::new);
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElseThrow(CartItemNotFoundException::new);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem item : cartItems){
            if(item.getCart().equals(cart)) cartItemDtos.add(CartItemDto.toDto(item));
        }
        if(cartItems.isEmpty()) throw new CartEmptyException();
        return cartItemDtos;
    }

    @Transactional
    public List<CartItemDto> checkPayment(){
        User user = getUserInfo();
        Cart cart = cartRepository.findByBuyer(user).orElseThrow(CartNotFoundException::new);
        Integer price = 0;
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElseThrow(CartItemNotFoundException::new);
        for(CartItem cartItem : cartItems){
            price  = price + cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        if(cartItems.isEmpty()) throw new CartEmptyException();
        cartRepository.delete(cart);
        Cart newCart = Cart.builder()
                .buyer(user).build();
        cartRepository.save(newCart);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem1 : cartItems){
            cartItemDtos.add(CartItemDto.toDto(cartItem1));
        }
        return cartItemDtos;
    }

    @Transactional
    public List<CartItemDto> includeProductToCart(Long productId, Integer howMany){
        User user = getUserInfo();
        Cart cart = cartRepository.findByBuyer(user).orElseThrow(CartNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if(howMany > product.getQuantity()){throw new ProductNotEnoughException();}
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(
                CartItem.builder().product(product).quantity(0).cart(cart).build()
        );
        cartItem.setQuantity(cartItem.getQuantity() + howMany);
        cartItemRepository.save(cartItem);
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElseThrow(CartItemNotFoundException::new);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem1 : cartItems){
            cartItemDtos.add(CartItemDto.toDto(cartItem1));
        }
        return cartItemDtos;
    }

    @Transactional
    public List<CartItemDto> excludeProductFromCart(Long productId){
        User user = getUserInfo();
        Cart cart = cartRepository.findByBuyer(user).orElseThrow(CartNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseThrow(CartItemNotFoundException::new);
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        cartItemRepository.delete(cartItem);
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElseThrow(CartItemNotFoundException::new);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem1 : cartItems){
            cartItemDtos.add(CartItemDto.toDto(cartItem1));
        }
        return cartItemDtos;
    }

    private User getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return user;
    }

}