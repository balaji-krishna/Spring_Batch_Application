package com.springBatch.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstChunkItemProcessor implements ItemProcessor<Integer, Long> {

	public static final Logger LOGGER = LoggerFactory.getLogger(FirstChunkItemProcessor.class);

	@Override
	public Long process(Integer item) throws Exception {
		LOGGER.info("Inside First Chunk Item Processor");
		return Long.valueOf(item + 500);
	}

}
