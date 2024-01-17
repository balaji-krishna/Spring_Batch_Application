package com.springBatch.app.config;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.springBatch.app.entity.Customer;
import com.springBatch.app.listener.FlatFileSkipListener;

@Configuration
public class FlatFileChunkConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private FlatFileSkipListener flatFileSkipListener;
	
	@Bean
	public Job flatFileChunkJob() {
		return jobBuilderFactory.get("Flat File Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(flatFileChunkStep())
				.build();
	}
	
	@Bean
	public Step flatFileChunkStep() {
		return stepBuilderFactory.get("Flat File Chunk Step")
				.<Customer, Customer>chunk(2)
				.reader(flatFileItemReader(null))
				.writer(flatFileItemWriter(null))
				.faultTolerant()
				.skip(Throwable.class)
				.skipPolicy(new AlwaysSkipItemSkipPolicy())
				.listener(flatFileSkipListener)
				.build();
	}
	
	@StepScope
	@Bean
	public FlatFileItemReader<Customer> flatFileItemReader(
			@Value("#{jobParameters['inputCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemReader<Customer> flatFileItemReader = 
				new FlatFileItemReader<Customer>();
		
		flatFileItemReader.setResource(fileSystemResource);
		
		DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<Customer>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<Customer>();
		fieldSetMapper.setTargetType(Customer.class);

		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	@StepScope
	@Bean
	public FlatFileItemWriter<Customer> flatFileItemWriter(
			@Value("#{jobParameters['outputCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemWriter<Customer> flatFileItemWriter = 
				new FlatFileItemWriter<Customer>();
		
		flatFileItemWriter.setResource(fileSystemResource);
		
		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("Id,First Name,Last Name,Email");
			}
		});
		
		BeanWrapperFieldExtractor<Customer> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<Customer>();
		beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});
		
		DelimitedLineAggregator<Customer> delimitedLineAggregator = new DelimitedLineAggregator<Customer>();
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		flatFileItemWriter.setLineAggregator(delimitedLineAggregator);
		
		flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
			@Override
			public void writeFooter(Writer writer) throws IOException {
				writer.write("Created @ " + new Date());
			}
		});
		
		return flatFileItemWriter;
	}
	
}
