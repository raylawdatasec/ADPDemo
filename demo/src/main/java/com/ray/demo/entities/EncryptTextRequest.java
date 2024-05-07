package com.ray.demo.entities;

import javax.persistence.Entity;

@Entity
public class EncryptTextRequest {
    private String message;
    private String keyName;

    EncryptTextRequest(String message, String keyName) {
        this.message = message;
        this.keyName = keyName;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
