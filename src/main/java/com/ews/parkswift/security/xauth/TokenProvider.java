package com.ews.parkswift.security.xauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenProvider {

    private final String secretKey;
    private final int tokenValidity;

    public TokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
    }

    public Token createToken(UserDetails userDetails) {
        long expires = System.currentTimeMillis() + 1000L * tokenValidity;
        String token = userDetails.getUsername() + ":" + expires + ":" + computeSignature(userDetails, expires);
        return new Token(token, expires);
    }
    
    public Token createTokenMobile(UserDetails userDetails) {
		
    	return new Token(userDetails.getUsername() + ":" + computeSignature(userDetails));
	}

    private String computeSignature(UserDetails userDetails) {
    	StringBuilder signatureBuilder = buildSignature(userDetails, null);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));

	}

	public String computeSignature(UserDetails userDetails, long expires) {
        StringBuilder signatureBuilder = buildSignature(userDetails, expires);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

	private StringBuilder buildSignature(UserDetails userDetails, Long expires) {
		StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername()).append(":");
        if(expires!=null)
        	signatureBuilder.append(expires).append(":");
        signatureBuilder.append(userDetails.getPassword()).append(":");
        signatureBuilder.append(secretKey);
		return signatureBuilder;
	}

    public String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }
        String[] parts = authToken.split(":");
        return parts[0];
    }

    public boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];
        String signatureToMatch = computeSignature(userDetails, expires);
        return expires >= System.currentTimeMillis() && signature.equals(signatureToMatch);
    }
    
    public boolean validateTokenMobile(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        String signature = parts[1];
        String signatureToMatch = computeSignature(userDetails);
        return signature.equals(signatureToMatch);
    }

	
}
