package com.springBatch.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.springBatch.app.entity.Customer;

@Configuration
public class XmlChunkJobConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job xmlChunkJob() {
		return jobBuilderFactory.get("Xml Chunk Job")
				.incrementer(null)
				.start(xmlChunkStep())
				.build();
	}
	
	@Bean
	public Step xmlChunkStep() {
		return stepBuilderFactory.get("Xml Chunk Step")
				.<Customer, Customer>chunk(2)
				.reader(staxEventItemReader(null))
				.writer(staxEventItemWriter(null))
				.build();
	}
	
	@StepScope
	@Bean
	public StaxEventItemReader<Customer> staxEventItemReader(
			@Value("#{jobParameters['inputXml']}") FileSystemResource fileSystemResource) {
		StaxEventItemReader<Customer> staxEventItemReader = 
				new StaxEventItemReader<Customer>();
		
		staxEventItemReader.setResource(fileSystemResource);
		staxEventItemReader.setStrict(false);
		staxEventItemReader.setFragmentRootElementName("customer");
		staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller() {
			{
				setClassesToBeBound(Customer.class);
			}
		});
		
		return staxEventItemReader;
	}
	
	@StepScope
	@Bean
	public StaxEventItemWriter<Customer> staxEventItemWriter(
			@Value("#{jobParameters['outputXml']}") FileSystemResource fileSystemResource) {
		StaxEventItemWriter<Customer> staxEventItemWriter = 
				new StaxEventItemWriter<Customer>();
		
		staxEventItemWriter.setResource(fileSystemResource);
		staxEventItemWriter.setRootTagName("customers");
		
		staxEventItemWriter.setMarshaller(new Jaxb2Marshaller() {
			{
				setClassesToBeBound(Customer.class);
			}
		});
		
		return staxEventItemWriter;
	}

}
