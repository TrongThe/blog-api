package com.example.blogapi.exception;

import com.example.blogapi.enums.MessageKey;

public class ConflictException extends RuntimeException {

    private final MessageKey messageKey;

    public ConflictException(MessageKey messageKey) {
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey(){
        return messageKey;
    }
}
