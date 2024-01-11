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

import com.springBatch.app.entity.CustomerCsv;
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
				.<CustomerResponse, CustomerCsv>chunk(2)
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
	public FlatFileItemWriter<CustomerCsv> flatFileRestItemWriter(
			@Value("#{jobParameters['outputRestCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemWriter<CustomerCsv> flatFileItemWriter = 
				new FlatFileItemWriter<CustomerCsv>();
		
		flatFileItemWriter.setResource(fileSystemResource);
		
		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("Id,First Name,Last Name,Email");
			}
		});
		
		BeanWrapperFieldExtractor<CustomerCsv> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<CustomerCsv>();
		beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});
		
		DelimitedLineAggregator<CustomerCsv> delimitedLineAggregator = new DelimitedLineAggregator<CustomerCsv>();
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
				.<CustomerCsv, CustomerCsv>chunk(2)
				.reader(flatFileRestItemReader(null))
				.writer(restItemWriterAdapter())
				.build();
	}
	
	@Bean
	@StepScope
	public ItemWriterAdapter<CustomerCsv> restItemWriterAdapter() {
		ItemWriterAdapter<CustomerCsv> itemWriterAdapter = 
				new ItemWriterAdapter<CustomerCsv>();
		
		itemWriterAdapter.setTargetObject(restCallService);
		itemWriterAdapter.setTargetMethod("restCallToCreateCustomer");
		
		return itemWriterAdapter;
	}
	
	@StepScope
	@Bean
	public FlatFileItemReader<CustomerCsv> flatFileRestItemReader(
			@Value("#{jobParameters['inputRestCsvFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemReader<CustomerCsv> flatFileItemReader = 
				new FlatFileItemReader<CustomerCsv>();
		
		flatFileItemReader.setResource(fileSystemResource);
		
		DefaultLineMapper<CustomerCsv> defaultLineMapper = new DefaultLineMapper<CustomerCsv>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<CustomerCsv> fieldSetMapper = new BeanWrapperFieldSetMapper<CustomerCsv>();
		fieldSetMapper.setTargetType(CustomerCsv.class);

		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	

}
