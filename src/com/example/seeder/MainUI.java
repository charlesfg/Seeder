package com.example.seeder;

import java.util.ArrayList;
import java.util.List;

import com.brightcove.zencoder.client.ZencoderClient;
import com.brightcove.zencoder.client.ZencoderClientException;
import com.brightcove.zencoder.client.model.ContainerFormat;
import com.brightcove.zencoder.client.request.*;
import com.example.model.Model;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("seeder")
public class MainUI extends UI {

	public Model model = Model.getInstance();
	
	VerticalLayout mainLayout;
	
	@Override
	protected void init(VaadinRequest request) {
		initiateMainLayout();
		
		Label restForm = new Label(buildHTMLForm(), ContentMode.HTML);
		Button buttonZen = buildZencoderButton();
		Link link = buildPlayerLink();
			
		mainLayout.addComponent(restForm);
		mainLayout.addComponent(buttonZen);
		mainLayout.addComponent(link);
	}
	
	public Link buildPlayerLink(){
		return new Link("To Player", 
				new ExternalResource("http://d1sx91gvv4q2wn.cloudfront.net/test.html"));
	}
	
	public Button buildZencoderButton(){
		return new Button("Zencoder",
				new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				ZencoderManager.decodeVideo();
				mainLayout.addComponent(new Label("Zencoder Request Sent!"));
			}
		});	
	}
	
	public Label buildHTMLForm() {
		return  new Label("<form action=\"http://" + model.bucketName + ".s3.amazonaws.com/\" method=\"post\" enctype=\"multipart/form-data\">"+
		    "<input type=\"hidden\" name=\"key\" value=\"video.dv\" /><br />"+
		    "<input type=\"hidden\" name=\"acl\" value=\"public-read\" />" +
		    //"<input type=\"hidden\" name=\"success_action_redirect\" value=\"/encode\" />" +
		    "<input type=\"hidden\" name=\"AWSAccessKeyId\" value=\""+ model.accessKey +"\" />" +
		    "<input type=\"hidden\" name=\"Policy\" value=\""+ model.policy +"\" />" +
		    "<input type=\"hidden\" name=\"Signature\" value=\""+ model.signedPolicy +"\" />" +
		    "File: <input type=\"file\" name=\"file\" /> <br />" +
		    "<input type=\"submit\" name=\"submit\" value=\"Upload to Amazon S3\" />" +
		"</form>", ContentMode.HTML);
	}
	
	public void initiateMainLayout(){
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		setContent(mainLayout);
		
	}
}