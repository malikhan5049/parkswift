package com.ews.parkswift.security.xauth;

/**
 * The security token.
 */
public class Token {

    String token;
    long expires;

    public Token(String token, long expires){
        this.token = token;
        this.expires = expires;
    }
    
    public Token(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public long getExpires() {
        return expires;
    }
}
