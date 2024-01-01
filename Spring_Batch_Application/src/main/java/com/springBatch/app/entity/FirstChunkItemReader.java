package com.springBatch.app.entity;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class FirstChunkItemReader implements ItemReader<Integer> {
	
	List<Integer> integerList = Arrays.asList(101, 102, 103, 104, 105);
	int x = 0;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("Inside First Chunk Item Reader");
		Integer value;
		if(x < integerList.size()) {
			value = integerList.get(x);
			x++;
			return value;
		}
		x = 0;
		return null;
	}

}
