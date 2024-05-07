package com.ray.demo.entities;

public class DecryptTextResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DecryptTextResponse(String message) {
        this.message = message;
    }
}
