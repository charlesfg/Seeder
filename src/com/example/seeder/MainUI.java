package com.example.seeder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.brightcove.zencoder.client.ZencoderClient;
import com.brightcove.zencoder.client.ZencoderClientException;
import com.brightcove.zencoder.client.model.ContainerFormat;
import com.brightcove.zencoder.client.request.*;
import com.brightcove.zencoder.client.response.ZencoderCreateJobResponse;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("seeder")
public class MainUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

//		final Label label = new Label(
//        		"<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">" +
//                "<input type=\"file\" name=\"myImage\" />" +
//                "<input type=\"hidden\" name=\"uploadKey\" value=\"" + uploadKey +"\" />" +
//                "<input type=\"submit\" value=\"Upload\" />" +
//                "</form>", Label.CONTENT_XHTML);

		String bucketName     = "cassiovideo";
		String accessKey      = "AKIAIOSVOU76NK76N3LQ";
		String secretKeyName  = "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";
		String uploadFileName = "C:\\Users\\Cassio\\Desktop\\ftio.wma";
		

		String policy = S3SignManager.StringToBase64String(makePolicy());
		String signedPolicy = S3SignManager.sign(secretKeyName,policy); 
		
		policy = S3SignManager.StringToBase64String(makePolicy());
		String signedPolicy2 = S3SignManager.sign(policy); 
		
		policy = S3SignManager.StringToBase64String(makePolicy());
		String signedPolicy3 = S3SignManager.sign2(makePolicy()); 
		

		System.out.println(policy);
		
		System.out.println(signedPolicy);
		System.out.println(signedPolicy2);
		System.out.println(signedPolicy3);
		System.out.println(signedPolicy.equals(signedPolicy2) + " . " + signedPolicy3.equals(signedPolicy2) + " . " +signedPolicy.equals(signedPolicy3));
		final Label label = new Label(
				"<form action=\"http://" + bucketName + ".s3.amazonaws.com/\" method=\"post\" enctype=\"multipart/form-data\">"+
				    "Key to upload: <input type=\"input\" name=\"key\" value=\"video.flv\" /><br />"+
				    "<input type=\"hidden\" name=\"acl\" value=\"public-read\" />" +
				    //"<input type=\"hidden\" name=\"success_action_redirect\" value=\"http://johnsmith.s3.amazonaws.com/successful_upload.html\" />" +
				    "<input type=\"hidden\" name=\"AWSAccessKeyId\" value=\""+ accessKey +"\" />" +
				    "<input type=\"hidden\" name=\"Policy\" value=\""+ policy +"\" />" +
				    "<input type=\"hidden\" name=\"Signature\" value=\""+ signedPolicy +"\" />" +
				    "File: <input type=\"file\" name=\"file\" /> <br />" +
				    "<input type=\"submit\" name=\"submit\" value=\"Upload to Amazon S3\" />" +
				"</form>", ContentMode.HTML);
		
		Button buttonS3 = new Button("S3 test");
		buttonS3.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Sending Request"));
					//System.out.println(S3SignManager.sign());
					//UploadFileToS3();
			}
		});
		Button buttonZen = new Button("Zencoder");
		buttonZen.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Sending Request"));
				try {
					sendRequest();
					layout.addComponent(new Label("Request Sent!"));
				} catch (ZencoderClientException e) {
					layout.addComponent(new Label("Request Failed"));
					e.printStackTrace();
				}
			}
		});
		layout.addComponent(label);
		//layout.addComponent(buttonS3);
		layout.addComponent(buttonZen);
	}
	
	public void sendRequest() throws ZencoderClientException{
		final ZencoderClient client = new ZencoderClient("bd06e2923f2fc8173f601f3c2f765063");
		
		ZencoderCreateJobRequest job = new ZencoderCreateJobRequest();
		job.setInput("s3://cassiovideo/Acroba.3gp");
		List<ZencoderOutput> outputs = new ArrayList<ZencoderOutput>();

		ZencoderOutput output1 = new ZencoderOutput();
        output1.setBaseUrl("s3://cassiovideo/");
        output1.setFormat(ContainerFormat.MP4);
        outputs.add(output1);

		job.setOutputs(outputs);
		ZencoderCreateJobResponse response = client.createZencoderJob(job);
		
		
	}

	public void UploadFileToS3(){
		
		String bucketName     = "cassiovideo";
		String keyName        = "AKIAIOSVOU76NK76N3LQ";
		String secretKeyName  = "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";
		String uploadFileName = "C:\\Users\\Cassio\\Desktop\\ftio.wma";
		AWSCredentials myCredentials = new BasicAWSCredentials(keyName,secretKeyName);
		AmazonS3 s3client = new AmazonS3Client(myCredentials);  
		
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
    		s3client.putObject(new PutObjectRequest(bucketName,keyName,file));

         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
	}
	
	public String makePolicy() {

		String policy =
			      "{\"expiration\": \"2014-02-18T12:00:00.000Z\"," +
			        "\"conditions\": [" +
			          "{\"bucket\": \"cassiovideo\"}," +
			          "[\"starts-with\", \"$key\", \"v\"]," +
			          "{\"acl\": \"public-read\"}" +
			        "]" +
			      "}";
		
		JSONObject result = new JSONObject();
		try {
			result.put("expiration", "2014-02-18T12:00:00.000Z");

			// Add conditions
			JSONObject conditions = new JSONObject();
				conditions.put("bucket", "cassiovideo");
			result.put("conditions", conditions);

//			// Accumulate values in an array
//			json.accumulate("passenger", "George Washington");
//			json.accumulate("passenger", "Thomas Jefferson");

			// Passing a number to toString() adds indentation
			System.out.println(result.toString());
			return policy;
			//return result.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}