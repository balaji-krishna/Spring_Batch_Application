package com.springBatch.app.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class SecondChunkItemWriter implements ItemWriter<String> {

	public static final Logger LOGGER = LoggerFactory.getLogger(SecondChunkItemWriter.class);

	@Override
	public void write(List<? extends String> items) throws Exception {
		LOGGER.info("Inside Second Chunk Item Writer");
		items.stream().forEach(System.out::println);
	}

}
