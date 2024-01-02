package com.springBatch.app.config;

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

import com.springBatch.app.entity.CustomerJson;

@Configuration
public class JsonChunkConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
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
				.<CustomerJson, CustomerJson>chunk(2)
				.reader(jsonItemReader(null))
				.writer(jsonItemWriter(null))
				.build();
	}
	
	@StepScope
	@Bean
	public JsonItemReader<CustomerJson> jsonItemReader(
			@Value("#{jobParameters['inputJson']}") FileSystemResource fileSystemResource) {
		JsonItemReader<CustomerJson> jsonItemReader = 
				new JsonItemReader<CustomerJson>();
		
		jsonItemReader.setResource(fileSystemResource);
		jsonItemReader.setJsonObjectReader(
				new JacksonJsonObjectReader<>(CustomerJson.class));
		
		return jsonItemReader;
	}
	
	@StepScope
	@Bean
	public JsonFileItemWriter<CustomerJson> jsonItemWriter(
			@Value("#{jobParameters['outputJson']}") FileSystemResource fileSystemResource) {
		JsonFileItemWriter<CustomerJson> jsonFileItemWriter = 
				new JsonFileItemWriter<>(fileSystemResource, 
						new JacksonJsonObjectMarshaller<CustomerJson>());
		
		return jsonFileItemWriter;
	}

}
