package com.springBatch.app.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import com.springBatch.app.entity.CustomerJson;

@Component
public class JsonSkipListener implements SkipListener<CustomerJson, CustomerJson> {

	@Override
	public void onSkipInRead(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSkipInWrite(CustomerJson item, Throwable t) {
		createFile("D:\\\\Eclipse\\\\FirstWorkspace\\\\Git\\\\Spring_Batch_Application\\\\Spring_Batch_Application\\\\OutputFiles\\\\SkippedBadJsonInputWrite.txt", 
				item.toString());
	}

	@Override
	public void onSkipInProcess(CustomerJson item, Throwable t) {
		// TODO Auto-generated method stub
		
	}
	
	public void createFile(String filePath, String data) {
		try(FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + "," + new Date() + "\n");
		}catch(Exception e) {
			
		}
	}

}