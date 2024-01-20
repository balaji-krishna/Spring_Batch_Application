package com.springBatch.app.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.springBatch.app.entity.Customer;

@Configuration
public class JpaChunkConfig extends DefaultBatchConfigurer {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("mysqlEntityManagerFactory")
	private EntityManagerFactory mysqlEntityManagerFactory;
	
	@Autowired
	@Qualifier("newmysqlEntityManagerFactory")
	private EntityManagerFactory newmysqlEntityManagerFactory;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;
	
	@Bean
	public Job jpaChunkJob() {
		return jobBuilderFactory.get("JPA Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(jpaChunkStep())
				.build();
	}
	
	@Bean
	public Step jpaChunkStep() {
		return stepBuilderFactory.get("JPA Chunk Step")
				.<Customer, Customer>chunk(3)
				.reader(jpaCursorItemReader())
				.writer(jpaItemWriter())
				.transactionManager(jpaTransactionManager)
				.build();
	}
	
	public JpaCursorItemReader<Customer> jpaCursorItemReader() {
		JpaCursorItemReader<Customer> jpaCursorItemReader = 
				new JpaCursorItemReader<Customer>();
		
		jpaCursorItemReader.setEntityManagerFactory(mysqlEntityManagerFactory);
		
		jpaCursorItemReader.setQueryString("From Customer");
		
		return jpaCursorItemReader;
	}
	
	public JpaItemWriter<Customer> jpaItemWriter() {
		JpaItemWriter<Customer> jpaItemWriter = 
				new JpaItemWriter<Customer>();
		
		jpaItemWriter.setEntityManagerFactory(newmysqlEntityManagerFactory);
		
		return jpaItemWriter;
	}

}
