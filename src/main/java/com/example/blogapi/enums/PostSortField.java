package com.example.blogapi.enums;

public enum PostSortField {

    CREATED_AT("createdAt"),
    TITLE("title"),
    UPDATED_AT("updateAt");

    private final String field;

    PostSortField(String field){
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
