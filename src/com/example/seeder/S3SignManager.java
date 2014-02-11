package com.example.seeder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class S3SignManager {
	
	private static final String UTF8 = "UTF-8";
	private static final String HMACSHA1 = "HmacSHA1";

	public static String sign(String secret, String data) {
		
		byte[] policyBytes = toUTF8Bytes(data);
		byte[] secretBytes = toUTF8Bytes(secret);
	    
		byte[] signature = computeSignature(policyBytes,secretBytes);
		
		return new String(Base64.encodeBase64(signature));
	}

	public static String StringToBase64String(String data) {
		return new String(StringToBase64Bytes(data));
	}

	public static byte[] StringToBase64Bytes(String data) {
		return Base64.encodeBase64(toUTF8Bytes(data));
	}

	public static byte[] toUTF8Bytes(String data) {
		try {
			
			return data.getBytes(UTF8);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] computeSignature(byte[] policyBytes, byte[] secretBytes) {
	    try {
	    	
	    	SecretKeySpec signingKey = new SecretKeySpec(secretBytes, HMACSHA1);
	    	Mac mac = Mac.getInstance(HMACSHA1);
	    	
			mac.init(signingKey);
		    return mac.doFinal(policyBytes);
		    
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	    return null;
	}
}
