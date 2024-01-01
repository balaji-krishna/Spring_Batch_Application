package com.springBatch.app.entity;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SecondChunkItemProcessor implements ItemProcessor<String, String> {

	@Override
	public String process(String item) throws Exception {
		System.out.println("Inside Second Chunk Item Processor");
		return String.format(item + " is a valid number!");
	}

}
