package com.springBatch.app.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.springBatch.app.entity.Customer;
import com.springBatch.app.listener.JsonSkipListener;

@Configuration
public class JsonChunkConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JsonSkipListener jsonSkipListener;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(JsonChunkConfig.class);
	
	@Bean
	public Job jsonChunkJob() {
		return jobBuilderFactory.get("Json Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(jsonChunkStep())
				.build();
	}
	
	@Bean
	public Step jsonChunkStep() {
		return stepBuilderFactory.get("Json Chunk Step")
				.<Customer, Customer>chunk(2)
				.reader(jsonItemReader(null))
				.writer(jsonItemWriter(null))
				.faultTolerant()
				.skip(Throwable.class)
				.skipLimit(100)
				.retryLimit(3)
				.retry(Throwable.class)
				.listener(jsonSkipListener)
				.build();
	}
	
	@StepScope
	@Bean
	public JsonItemReader<Customer> jsonItemReader(
			@Value("#{jobParameters['inputJson']}") FileSystemResource fileSystemResource) {
		JsonItemReader<Customer> jsonItemReader = 
				new JsonItemReader<Customer>();
		
		jsonItemReader.setResource(fileSystemResource);
		jsonItemReader.setJsonObjectReader(
				new JacksonJsonObjectReader<>(Customer.class));
		
		return jsonItemReader;
	}
	
	@StepScope
	@Bean
	public JsonFileItemWriter<Customer> jsonItemWriter(
			@Value("#{jobParameters['outputJson']}") FileSystemResource fileSystemResource) {
		JsonFileItemWriter<Customer> jsonFileItemWriter = 
				new JsonFileItemWriter<Customer>(fileSystemResource, 
						new JacksonJsonObjectMarshaller<Customer>()) {
			
			@Override
			public String doWrite(List<? extends Customer> items) {
				items.stream().forEach(item -> {
					if(item.getId() == 3) {
						LOGGER.info("Inside jsonFileItemWriter");
						throw new NullPointerException();
					}
				});
				return super.doWrite(items);
			}
		};
		
		return jsonFileItemWriter;
	}

}
