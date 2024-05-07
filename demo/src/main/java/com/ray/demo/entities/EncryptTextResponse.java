package com.ray.demo.entities;

public class EncryptTextResponse {

    private String encryptedHexText;
    private String iv;

    public EncryptTextResponse(String encryptedHexText, String iv) {
        this.encryptedHexText = encryptedHexText;
        this.iv = iv;
    }


    public String getEncryptedHexText() {
        return encryptedHexText;
    }

    public void setEncryptedHexText(String encryptedHexText) {
        this.encryptedHexText = encryptedHexText;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
