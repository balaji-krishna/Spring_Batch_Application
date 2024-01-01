package com.springBatch.app.entity;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstChunkItemProcessor implements ItemProcessor<Integer, Long> {

	@Override
	public Long process(Integer item) throws Exception {
		System.out.println("Inside First Chunk Item Processor");
		return Long.valueOf(item + 500);
	}

}
