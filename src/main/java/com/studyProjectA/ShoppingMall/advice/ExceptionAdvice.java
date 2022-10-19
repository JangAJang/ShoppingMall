package com.studyProjectA.ShoppingMall.advice;

import com.studyProjectA.ShoppingMall.excpetion.*;
import com.studyProjectA.ShoppingMall.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        return Response.failure(500, e.getMessage().toString());
    }

    //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    //404 응답
    //요청한 유저 정보를 찾을 수 없음

    // memberNotFoundException()랑 userNotFoundException(e)랑 겹치는 내용인거 같은데 ??(수정필요하면 코드리뷰 부탁드립니당)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {
        return Response.failure(404, "유저를 찾을 수 없습니다 ");
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reviewNotFoundException(){
        return Response.failure(404, "리뷰를 찾을 수 없습니다.");
    }


    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response productNotFoundException() {
        return Response.failure(404, "물품을 찾을 수 없습니다.");
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cartItemNotFoundExcpetion(){
        return Response.failure(404, "카트에서 품목을 찾을 수 없습니다. ");
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cartNotFoundExcpetion(){
        return Response.failure(404, "카트를 찾을 수 없습니다. ");
    }

    @ExceptionHandler(ProductNotEnoughException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response productNotEnoughExcpetion(){
        return Response.failure(404, "물품의 수량이 부족해 장바구니에 담을 수 없습니다. ");
    }

    @ExceptionHandler(CartEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cartEmptyExcpetion(){
        return Response.failure(404, "카트가 비었습니다. ");
    }

    @ExceptionHandler(UserNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response userNotEqualsException() {
        return Response.failure(401, "권한이 없습니다.");
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public Response usernameAlreadyExistsException(){
        return Response.failure(404, "이미 존재하는 계정입니다. ");
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public Response emailAlreadyExistsException(){
        return Response.failure(404, "이미 존재하는 이메일입니다. ");
    }
}