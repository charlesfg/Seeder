package com.example.seeder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.geronimo.mail.util.Base64Encoder;
import sun.misc.BASE64Encoder;

public class S3SignManager {
	private static final String UTF8 = "UTF-8";
	private static final String HMACSHA1 = "HmacSHA1";

	public static String sign2(String policy) {
		String secret = "adPfvk1tv274rMDlA27JaFIP/D4HUrFyJ3vhAYe1";
		
		// Calculate policy and signature values from the given policy document and AWS credentials.
	    String policyBase64;
	    String signature = "";
		try {
			
			policyBase64 = com.google.appengine.repackaged.com.google.api.client.util.Base64.encodeBase64String(policy.getBytes("UTF-8"));

		    Mac hmac = Mac.getInstance("HmacSHA1");
		    hmac.init(new SecretKeySpec(
		    		secret.getBytes("UTF-8"), "HmacSHA1"));
		    signature = new String(
		        Base64.encodeBase64(hmac.doFinal(policyBase64.getBytes("UTF-8"))));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	    return signature;
	}
//	
	public static String sign(String policy){
		String secret =  "adPfvk1tv274rMDlA27JaFIP/D4HUrFyJ3vhAYe1";
		
		//String policy;
		String signature ="";
		try {
			//policy = new String(Base64.encodeBase64(policy_document.getBytes("UTF-8")));
			Mac hmac = Mac.getInstance("HmacSHA1");
			hmac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA1"));
			signature = new String(Base64.encodeBase64(hmac.doFinal(policy.getBytes("UTF-8"))));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

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
