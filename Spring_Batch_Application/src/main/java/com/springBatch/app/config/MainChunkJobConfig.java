package com.springBatch.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.springBatch.app.listener.ChunkJobListener;
import com.springBatch.app.listener.ChunkStepListener;
import com.springBatch.app.processor.FirstChunkItemProcessor;
import com.springBatch.app.processor.SecondChunkItemProcessor;
import com.springBatch.app.reader.FirstChunkItemReader;
import com.springBatch.app.reader.SecondChunkItemReader;
import com.springBatch.app.writer.FirstChunkItemWriter;
import com.springBatch.app.writer.SecondChunkItemWriter;

@EnableScheduling
@Configuration
public class MainChunkJobConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private FirstChunkItemReader firstChunkItemReader;
	
	@Autowired
	private FirstChunkItemProcessor firstChunkItemProcessor;
	
	@Autowired
	private FirstChunkItemWriter firstChunkItemWriter;
	
	@Autowired
	private SecondChunkItemReader secondChunkItemReader;
	
	@Autowired
	private SecondChunkItemProcessor secondChunkItemProcessor;
	
	@Autowired
	private SecondChunkItemWriter secondChunkItemWriter;
	
	@Autowired
	private ChunkJobListener chunkJobListener;
	
	@Autowired
	private ChunkStepListener chunkStepListener;
	
	@Bean
	public Job mainChunkJob() {
		return jobBuilderFactory.get("Main Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())
				.next(secondChunkStep())
				.listener(chunkJobListener)
				.build();
	}
	
	@Bean
	public Step firstChunkStep() {
		return stepBuilderFactory.get("First Chunk Step")
				.<Integer, Long>chunk(3)
				.reader(firstChunkItemReader)
				.processor(firstChunkItemProcessor)
				.writer(firstChunkItemWriter)
				.listener(chunkStepListener)
				.build();
	}
	
	@Bean
	public Step secondChunkStep() {
		return stepBuilderFactory.get("Second Chunk Step")
				.<String, String>chunk(2)
				.reader(secondChunkItemReader)
				.processor(secondChunkItemProcessor)
				.writer(secondChunkItemWriter)
				.listener(chunkStepListener)
				.build();
	}

}
