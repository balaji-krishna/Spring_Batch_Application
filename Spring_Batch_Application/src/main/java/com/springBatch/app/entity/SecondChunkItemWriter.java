package com.springBatch.app.entity;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class SecondChunkItemWriter implements ItemWriter<String> {

	@Override
	public void write(List<? extends String> items) throws Exception {
		System.out.println("Inside Second Chunk Item Writer");
		items.stream().forEach(System.out::println);
	}

}
