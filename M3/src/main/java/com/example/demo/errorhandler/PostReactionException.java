package com.example.demo.errorhandler;

public class PostReactionException extends RuntimeException {

    public PostReactionException(String message) {
        super(message);
    }
}