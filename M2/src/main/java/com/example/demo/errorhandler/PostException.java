package com.example.demo.errorhandler;

public class PostException extends DemoException {
    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }
}
