package com.example.scame.savealife.data.entities;

public class TokenUpdateEntity {

    private String currentToken;

    private String oldToken;

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public void setOldToken(String oldToken) {
        this.oldToken = oldToken;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public String getOldToken() {
        return oldToken;
    }
}
