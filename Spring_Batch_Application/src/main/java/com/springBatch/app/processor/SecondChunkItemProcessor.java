package com.springBatch.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SecondChunkItemProcessor implements ItemProcessor<String, String> {

	public static final Logger LOGGER = LoggerFactory.getLogger(SecondChunkItemProcessor.class);

	@Override
	public String process(String item) throws Exception {
		LOGGER.info("Inside Second Chunk Item Processor");
		return String.format(item + " is a valid number!");
	}

}
