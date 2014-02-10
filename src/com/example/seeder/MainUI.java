package com.example.seeder;

import java.util.ArrayList;
import java.util.List;

import com.brightcove.zencoder.client.ZencoderClient;
import com.brightcove.zencoder.client.ZencoderClientException;
import com.brightcove.zencoder.client.model.ContainerFormat;
import com.brightcove.zencoder.client.request.*;
import com.brightcove.zencoder.client.response.ZencoderCreateJobResponse;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
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

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
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
		layout.addComponent(button);
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

}