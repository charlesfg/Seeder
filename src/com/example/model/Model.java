package com.example.model;
import com.example.seeder.AmazonS3Manager;


public class Model {
	
	private static Model instance;
	
	public String bucketName;
	public String accessKey;
	public String secretKeyName;
	
	public String policy;
	public String signedPolicy; 
	
	private Model(){

		bucketName     = "cassiovideo";
		accessKey      = "AKIAIOSVOU76NK76N3LQ";
		secretKeyName  = "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";
		
		policy = AmazonS3Manager.StringToBase64String(AmazonS3Manager.makePolicy());
		signedPolicy = AmazonS3Manager.sign(secretKeyName,policy);
	}

	public static Model getInstance() {
		if(instance==null)
			instance = new Model();
		return instance;
	}
}
