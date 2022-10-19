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
import org.hibernate.collection.internal.PersistentList;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemDto> getMyCart(User user){
        Cart cart = getCartByUser(user);
        List<CartItem> cartItems = getItemsFromCart(cart);
        return changeCartItemListToDto(cartItems);
    }

    @Transactional
    public Long checkPayment(User user){
        Cart cart = getCartByUser(user);
        List<CartItem> cartItems = getItemsFromCart(cart);
        Long price = getTotalPrice(cartItems);
        cartRepository.delete(cart);
        Cart newCart = Cart.builder()
                .buyer(user).build();
        cartRepository.save(newCart);
        return price;
    }

    @Transactional
    public List<CartItemDto> includeProductToCart(User user, Long productId, Integer howMany){
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if(howMany > product.getQuantity())throw new ProductNotEnoughException();
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(
                CartItem.builder().product(product).quantity(0).cart(cart).build()
        );
        cartItem.setQuantity(cartItem.getQuantity() + howMany);
        cartItemRepository.save(cartItem);
        List<CartItem> cartItems = getItemsFromCart(cart);
        return changeCartItemListToDto(cartItems);
    }

    @Transactional
    public List<CartItemDto> excludeProductFromCart(User user, Long productId){
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseThrow(CartItemNotFoundException::new);
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        cartItemRepository.delete(cartItem);
        List<CartItem> cartItems = getItemsFromCart(cart);
        return changeCartItemListToDto(cartItems);
    }

    public Cart getCartByUser(User user){
        return cartRepository.findByBuyer(user).orElseThrow(CartNotFoundException::new);
    }

    public List<CartItem> getItemsFromCart(Cart cart){
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElseThrow(CartItemNotFoundException::new);
        validateCartItemExistence(cartItems);
        return cartItems;
    }

    public List<CartItemDto> changeCartItemListToDto(List<CartItem> cartItems){
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            cartItemDtos.add(CartItemDto.toDto(cartItem));
        }
        return cartItemDtos;
    }

    public Long getTotalPrice(List<CartItem> cartItems){
        Long result = 0L;
        for(CartItem cartItem : cartItems){
            result = result + (long) cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return result;
    }

    public void validateCartItemExistence(List<CartItem> cartItems){
        if(cartItems.isEmpty()) throw new CartEmptyException();
    }

}