package com.example.blogapi.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageKey {

    POST_NOT_FOUND("post.notfound"),
    POST_CREATED("post.created"),
    POST_UPDATED("post.updated"),
    POST_DELETED("post.deleted"),
    POST_PUBLISHED("post.published"),
    POST_TITLE_REQUIRED("post.title.required"),
    POST_TITLE_MAX("post.title.max"),
    POST_CONTENT_REQUIRED("post.content.required"),
    POST_CATEGORY_REQUIRED("post.category.required"),
    POST_ALREADY_PUBLISHED("post.already.published"),

    CATEGORY_NOT_FOUND("category.notfound"),
    CATEGORY_CREATED("category.created"),
    CATEGORY_UPDATED("category.updated"),
    CATEGORY_DELETED("category.deleted"),
    CATEGORY_NAME_EXISTS("category.name.exists"),
    CATEGORY_NAME_REQUIRED("category.name.required"),
    CATEGORY_NAME_MAX("category.name.max"),
    CATEGORY_DESCRIPTION_MAX("category.description.max"),

    USER_NOT_FOUND("user.notfound"),
    USER_CREATED("user.created"),
    USER_ENABLED_REQUIRED("user.enabled.required"),
    USER_ROLE_REQUIRED("user.role.required"),

    COMMENT_CREATED("comment.created"),
    COMMENT_UPDATED("comment.updated"),
    COMMENT_DELETED("comment.deleted"),
    COMMENT_NOT_FOUND("comment.notfound"),
    COMMENT_CONTENT_REQUIRED("comment.content.required"),
    COMMENT_CONTENT_MAX("comment.content.max"),

    AUTH_INVALID("auth.invalid"),
    AUTH_USERNAME_EXISTS("auth.username.exists"),
    AUTH_EMAIL_EXISTS("auth.email.exists"),
    AUTH_REFRESH_TOKEN_INVALID("auth.refresh_token.invalid"),
    AUTH_REFRESH("auth.refresh"),
    AUTH_LOGOUT("auth.logout"),
    AUTH_ACCOUNT_DISABLED("auth.account.disabled"),

    ACCESS_DENIED("access.denied"),
    VALIDATION_FAILED("validation.failed"),
    INTERNAL_SERVER_ERROR("internal.server.error");

    private final String key;

    public static MessageKey fromKey(String key){
        for(MessageKey messageKey : values()){
            if(messageKey.key.equals(key)){
                return messageKey;
            }
        }

        throw new IllegalArgumentException("Unknown message key: " + key);
    }

}
