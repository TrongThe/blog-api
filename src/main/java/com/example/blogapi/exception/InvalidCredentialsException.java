package com.example.blogapi.exception;

import com.example.blogapi.enums.MessageKey;

public class InvalidCredentialsException extends RuntimeException{

    private final MessageKey messageKey;

    public InvalidCredentialsException(MessageKey messageKey){
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey(){
        return messageKey;
    }
}
