package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.CartDto;
import com.studyProjectA.ShoppingMall.dto.CartItemDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.CartItemRepository;
import com.studyProjectA.ShoppingMall.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemDto> showMyCartItems(User user){
        Cart myCart = cartRepository.findByBuyer(user.getUsername());
        List<CartItem> cartItems = cartItemRepository.findAllByCart(myCart);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItems.forEach(s->cartItemDtos.add(CartItemDto.toDto(s)));
        return cartItemDtos;
    }

    @Transactional
    public CartDto clearMyCart(long cartId, User user){
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->{
            return new IllegalArgumentException("장바구니를 찾을 수 없습니다. ");
        });
        cartRepository.delete(cart);
        Cart nCart = new Cart();
        nCart.setBuyer(user);
        cartRepository.save(nCart);
        return CartDto.toDto(nCart);
    }

    @Transactional
    public CartItemDto putInCart(Product product, User user){
        Cart cart = cartRepository.findByBuyer(user.getUsername());
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setId(product.getId());
        cartItemRepository.save(cartItem);
        return CartItemDto.toDto(cartItem);
    }

    @Transactional
    public String takeOutFromCart(long cartItem){
        cartItemRepository.findById(cartItem).orElseThrow(()->{
            return new IllegalArgumentException("장바구니에서 상품을 찾을 수 없습니다. ");
        });
        cartItemRepository.deleteById(cartItem);
        return "삭제완료";
    }
}
