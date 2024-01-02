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

import com.springBatch.app.entity.CustomerXml;

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
				.<CustomerXml, CustomerXml>chunk(2)
				.reader(staxEventItemReader(null))
				.writer(staxEventItemWriter(null))
				.build();
	}
	
	@StepScope
	@Bean
	public StaxEventItemReader<CustomerXml> staxEventItemReader(
			@Value("#{jobParameters['inputXml']}") FileSystemResource fileSystemResource) {
		StaxEventItemReader<CustomerXml> staxEventItemReader = 
				new StaxEventItemReader<CustomerXml>();
		
		staxEventItemReader.setResource(fileSystemResource);
		staxEventItemReader.setFragmentRootElementName("student");
		staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller() {
			{
				setClassesToBeBound(CustomerXml.class);
			}
		});
		
		return staxEventItemReader;
	}
	
	@StepScope
	@Bean
	public StaxEventItemWriter<CustomerXml> staxEventItemWriter(
			@Value("#{jobParameters['outputXml']}") FileSystemResource fileSystemResource) {
		StaxEventItemWriter<CustomerXml> staxEventItemWriter = 
				new StaxEventItemWriter<CustomerXml>();
		
		staxEventItemWriter.setResource(fileSystemResource);
		staxEventItemWriter.setRootTagName("students");
		
		staxEventItemWriter.setMarshaller(new Jaxb2Marshaller() {
			{
				setClassesToBeBound(CustomerXml.class);
			}
		});
		
		return staxEventItemWriter;
	}

}
