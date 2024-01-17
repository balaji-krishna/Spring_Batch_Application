package com.springBatch.app.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.springBatch.app.entity.Customer;

@Component
public class FlatFileSkipListener implements SkipListener<Customer, Customer> {

	@Override
	public void onSkipInRead(Throwable t) {
		if (t instanceof FlatFileParseException) {
			createFile(
					"D:\\Eclipse\\FirstWorkspace\\Git\\Spring_Batch_Application\\Spring_Batch_Application\\OutputFiles\\SkippedBadInputRead.txt",
					((FlatFileParseException) t).getInput());
		}
	}

	@Override
	public void onSkipInWrite(Customer item, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSkipInProcess(Customer item, Throwable t) {
		// TODO Auto-generated method stub

	}

	public void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + "," + new Date() + "\n");
		} catch (Exception e) {

		}
	}

}
