package com.example.seeder;

import java.util.ArrayList;
import java.util.List;

import com.brightcove.zencoder.client.ZencoderClient;
import com.brightcove.zencoder.client.ZencoderClientException;
import com.brightcove.zencoder.client.model.ContainerFormat;
import com.brightcove.zencoder.client.request.ZencoderCreateJobRequest;
import com.brightcove.zencoder.client.request.ZencoderOutput;

public class ZencoderManager {

	private static final String CLIENT_ID_KEY = "bd06e2923f2fc8173f601f3c2f765063";
	private static final String BASE_URL = "s3://cassiovideo/";
	private static final String INPUT_FILE_NAME = "video.dv";
	private static final String OUTPUT_FILE_NAME = "video.mp4";
	
	
	public static void decodeVideo(){
		
		ZencoderCreateJobRequest job = new ZencoderCreateJobRequest();
		
		job.setInput(BASE_URL + INPUT_FILE_NAME);
		job.setOutputs(buildOutputs());
		
		try {
			
			ZencoderClient client = new ZencoderClient(CLIENT_ID_KEY);
			client.createZencoderJob(job);
			
		} catch (Exception e) {e.printStackTrace();}		
	}
	
	private static List<ZencoderOutput> buildOutputs(){
		ZencoderOutput output1 = new ZencoderOutput();
        output1.setBaseUrl(BASE_URL);
        output1.setFilename(OUTPUT_FILE_NAME);
        output1.setFormat(ContainerFormat.MP4);
        
		List<ZencoderOutput> outputs = new ArrayList<ZencoderOutput>();
		outputs.add(output1);
		
		return outputs;
	}
}
