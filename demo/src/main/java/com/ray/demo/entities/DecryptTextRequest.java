package com.ray.demo.entities;

import javax.persistence.Entity;

@Entity
public class DecryptTextRequest {

    private String encryptedHexMessage;
    private String keyName;
    private String iv;

    public DecryptTextRequest(String encryptedHexMessage, String keyName, String iv) {
        this.encryptedHexMessage = encryptedHexMessage;
        this.keyName = keyName;
        this.iv = iv;
    }

    public String getEncryptedHexMessage() {
        return encryptedHexMessage;
    }

    public void setEncryptedHexMessage(String encryptedHexMessage) {
        this.encryptedHexMessage = encryptedHexMessage;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
