package com.springBatch.app.entity;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstChunkItemWriter implements ItemWriter<Long> {

	@Override
	public void write(List<? extends Long> items) throws Exception {
		System.out.println("Inside First Chunk Item Writer");
		items.stream().forEach(System.out::println);
	}

}
