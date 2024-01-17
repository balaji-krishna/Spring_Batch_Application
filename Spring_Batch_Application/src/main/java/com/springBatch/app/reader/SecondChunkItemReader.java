package com.springBatch.app.reader;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class SecondChunkItemReader implements ItemReader<String> {

	public static final Logger LOGGER = LoggerFactory.getLogger(SecondChunkItemReader.class);

	List<String> stringList = Arrays.asList("101", "102", "103", "104", "105");
	int x = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		LOGGER.info("Inside Second Chunk Item Reader");
		String value;
		if (x < stringList.size()) {
			value = stringList.get(x);
			x++;
			return value;
		}
		x = 0;
		return null;
	}

}
