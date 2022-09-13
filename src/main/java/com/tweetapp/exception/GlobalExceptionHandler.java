package com.tweetapp.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IncorrectOrDeletedTweetException.class)
    public void springHandleIncorrectOrDeletedTweet(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(),"INCORRECT_OR_DELETED_TWEET");
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public void springHandleUsernameAlreadyExistsException(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(),"USER_ALREADY_EXISTS");
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public void springHandleInvalidUsernameOrPasswordException(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(),"INVALID_CREDENTIALS");
    }



}