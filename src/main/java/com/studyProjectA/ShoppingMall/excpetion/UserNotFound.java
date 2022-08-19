package com.studyProjectA.ShoppingMall.excpetion;

import com.studyProjectA.ShoppingMall.response.Response;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFound extends RuntimeException{

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response UserNotFound(ChangeSetPersister.NotFoundException e) {
        return Response.failure(404, "유저를 찾을 수 없습니다 ");
    }

}
