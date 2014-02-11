package com.example.seeder;

import java.util.ArrayList;
import java.util.List;

import com.brightcove.zencoder.client.ZencoderClient;
import com.brightcove.zencoder.client.ZencoderClientException;
import com.brightcove.zencoder.client.model.ContainerFormat;
import com.brightcove.zencoder.client.request.*;
import com.brightcove.zencoder.client.response.ZencoderCreateJobResponse;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("seeder")
public class MainUI extends UI {

	String bucketName     = "cassiovideo";
	String accessKey      = "AKIAIOSVOU76NK76N3LQ";
	String secretKeyName  = "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";
	
	String policy = S3SignManager.StringToBase64String(makePolicy());
	String signedPolicy = S3SignManager.sign(secretKeyName,policy); 

	Label restForm = new Label(buildHTMLForm(), ContentMode.HTML);
	
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		setContent(layout);
		
		Button buttonZen = new Button("Zencoder");
		buttonZen.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Sending Request"));
				try {
					askZenconderToDecodeVideo();
					layout.addComponent(new Label("Zencoder Request Sent!"));
					
				} catch (ZencoderClientException e) {
					layout.addComponent(new Label("Zencoder Request Failed!"));
					e.printStackTrace();
				}
			}
		});	
		
		Link link = new Link("Click Me!", new ExternalResource("http://d1sx91gvv4q2wn.cloudfront.net/test.html"));
			
		layout.addComponent(restForm);
		layout.addComponent(buttonZen);
		layout.addComponent(link);
	}
	
	public String buildHTMLForm() {
		return "<form action=\"http://" + bucketName + ".s3.amazonaws.com/\" method=\"post\" enctype=\"multipart/form-data\">"+
		    "<input type=\"hidden\" name=\"key\" value=\"video.dv\" /><br />"+
		    "<input type=\"hidden\" name=\"acl\" value=\"public-read\" />" +
		    //"<input type=\"hidden\" name=\"success_action_redirect\" value=\"/encode\" />" +
		    "<input type=\"hidden\" name=\"AWSAccessKeyId\" value=\""+ accessKey +"\" />" +
		    "<input type=\"hidden\" name=\"Policy\" value=\""+ policy +"\" />" +
		    "<input type=\"hidden\" name=\"Signature\" value=\""+ signedPolicy +"\" />" +
		    "File: <input type=\"file\" name=\"file\" /> <br />" +
		    "<input type=\"submit\" name=\"submit\" value=\"Upload to Amazon S3\" />" +
		"</form>";
	}

	
	public void askZenconderToDecodeVideo() throws ZencoderClientException{
		//Creat a zencoder Cliente with the key assossiated with the account
		final ZencoderClient client = new ZencoderClient("bd06e2923f2fc8173f601f3c2f765063");
		
		ZencoderCreateJobRequest job = new ZencoderCreateJobRequest();
		job.setInput("s3://cassiovideo/video.dv");
		
		ZencoderOutput output1 = new ZencoderOutput();
        output1.setBaseUrl("s3://cassiovideo/");
        output1.setFilename("video.mp4");
        output1.setFormat(ContainerFormat.MP4);
        
		List<ZencoderOutput> outputs = new ArrayList<ZencoderOutput>();
        outputs.add(output1);

		job.setOutputs(outputs);
		client.createZencoderJob(job);		
	}
	
	public String makePolicy() {

		return   "{\"expiration\": \"2014-02-18T12:00:00.000Z\"," +
			        "\"conditions\": [" +
			          "{\"bucket\": \"cassiovideo\"}," +
			          "[\"starts-with\", \"$key\", \"\"]," +
			          "{\"acl\": \"public-read\"}" +
			        "]" +
			      "}";
	}
}