package com.example.blogapi.util;


import com.example.blogapi.enums.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {

    public final MessageSource messageSource;

    public String getMessage(
            MessageKey messageKey,
            Locale locale
    ){
        return messageSource.getMessage(
                messageKey.getKey(),
                null,
                locale
        );
    }

}
