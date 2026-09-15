package com.example.blogapi.exception;

import com.example.blogapi.enums.MessageKey;

public class ResourceNotFoundException extends RuntimeException{

    private final MessageKey messageKey;

    public ResourceNotFoundException(MessageKey messageKey){
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey(){
        return messageKey;
    }
}
