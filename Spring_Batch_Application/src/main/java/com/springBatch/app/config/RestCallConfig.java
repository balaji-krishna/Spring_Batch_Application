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
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
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

import com.springBatch.app.entity.CustomerRequest;
import com.springBatch.app.entity.CustomerResponse;
import com.springBatch.app.service.RestCallService;

@Configuration
public class RestCallConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private RestCallService restCallService;
	
	@Bean
	public Job restCallChunkJob() {
		return jobBuilderFactory.get("Rest Call Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(restCallFirstChunkStep())
				.next(restCallSecondChunkStep())
				.build();
	}
	
	@Bean
	public Step restCallFirstChunkStep() {
		return stepBuilderFactory.get("Rest Call Chunk Step")
				.<CustomerResponse, CustomerRequest>chunk(2)
				.reader(restItemReaderAdapter())
				.writer(flatFileRestItemWriter(null))
				.build();
	}
	
	@Bean
	@StepScope
	public ItemReaderAdapter<CustomerResponse> restItemReaderAdapter() {
		ItemReaderAdapter<CustomerResponse> itemReaderAdapter = 
				new ItemReaderAdapter<CustomerResponse>();
		
		itemReaderAdapter.setTargetObject(restCallService);
		itemReaderAdapter.setTargetMethod("getCustomers");
		
		return itemReaderAdapter;
	}
	
	@Bean
	@StepScope
	public FlatFileItemWriter<CustomerRequest> flatFileRestItemWriter(
			@Value("#{jobParameters['outputRestCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemWriter<CustomerRequest> flatFileItemWriter = 
				new FlatFileItemWriter<CustomerRequest>();
		
		flatFileItemWriter.setResource(fileSystemResource);
		
		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("Id,First Name,Last Name,Email");
			}
		});
		
		BeanWrapperFieldExtractor<CustomerRequest> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<CustomerRequest>();
		beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});
		
		DelimitedLineAggregator<CustomerRequest> delimitedLineAggregator = new DelimitedLineAggregator<CustomerRequest>();
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
	
	@Bean
	public Step restCallSecondChunkStep() {
		return stepBuilderFactory.get("Rest Call Chunk Step")
				.<CustomerRequest, CustomerRequest>chunk(2)
				.reader(flatFileRestItemReader(null))
				.writer(restItemWriterAdapter())
				.build();
	}
	
	@Bean
	@StepScope
	public ItemWriterAdapter<CustomerRequest> restItemWriterAdapter() {
		ItemWriterAdapter<CustomerRequest> itemWriterAdapter = 
				new ItemWriterAdapter<CustomerRequest>();
		
		itemWriterAdapter.setTargetObject(restCallService);
		itemWriterAdapter.setTargetMethod("restCallToCreateCustomer");
		
		return itemWriterAdapter;
	}
	
	@StepScope
	@Bean
	public FlatFileItemReader<CustomerRequest> flatFileRestItemReader(
			@Value("#{jobParameters['inputRestCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemReader<CustomerRequest> flatFileItemReader = 
				new FlatFileItemReader<CustomerRequest>();
		
		flatFileItemReader.setResource(fileSystemResource);
		
		DefaultLineMapper<CustomerRequest> defaultLineMapper = new DefaultLineMapper<CustomerRequest>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<CustomerRequest> fieldSetMapper = new BeanWrapperFieldSetMapper<CustomerRequest>();
		fieldSetMapper.setTargetType(CustomerRequest.class);

		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	

}
