package com.springBatch.app.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstChunkItemWriter implements ItemWriter<Long> {

	public static final Logger LOGGER = LoggerFactory.getLogger(FirstChunkItemWriter.class);

	@Override
	public void write(List<? extends Long> items) throws Exception {
		LOGGER.info("Inside First Chunk Item Writer");
		items.stream().forEach(System.out::println);
	}

}
