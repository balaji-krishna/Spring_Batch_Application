package com.springBatch.app.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.springBatch.app.entity.CustomerJdbc;

@Configuration
public class JdbcChunkJobConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DatabaseConfig databaseConfig;
		
	@Bean
	public Job jdbcChunkJob() {
		return jobBuilderFactory.get("JDBC Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(jdbcChunkStep())
				.build();
	}
	
	@Bean
	public Step jdbcChunkStep() {
		return stepBuilderFactory.get("JDBC Chunk Step")
				.<CustomerJdbc, CustomerJdbc>chunk(3)
				.reader(jdbcItemReader())
				.writer(jdbcItemWriter())
				.build();
	}
	
	@Bean
	public JdbcCursorItemReader<CustomerJdbc> jdbcItemReader() {
		JdbcCursorItemReader<CustomerJdbc> jdbcCursorItemReader = 
				new JdbcCursorItemReader<CustomerJdbc>();
		
		jdbcCursorItemReader.setDataSource(databaseConfig.datasource());
		jdbcCursorItemReader.setSql(
				"select id, first_name as firstName, last_name as lastName,"
				+ "email from customers_info");
		
		jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<CustomerJdbc>() {
			{
				setMappedClass(CustomerJdbc.class);
			}
		});
		
		return jdbcCursorItemReader;
	}
	
	@Bean
	public JdbcBatchItemWriter<CustomerJdbc> jdbcItemWriter() {
		JdbcBatchItemWriter<CustomerJdbc> jdbcBatchItemWriter = 
				new JdbcBatchItemWriter<CustomerJdbc>();
		
		jdbcBatchItemWriter.setDataSource(databaseConfig.newcustomerdatasource());
		jdbcBatchItemWriter.setSql(
				"insert into customers_info(id, first_name, last_name, email) "
				+ "values (?,?,?,?)");
		
		jdbcBatchItemWriter.setItemPreparedStatementSetter(
				new ItemPreparedStatementSetter<CustomerJdbc>() {
			
			@Override
			public void setValues(CustomerJdbc item, PreparedStatement ps) throws SQLException {
				ps.setLong(1, item.getId());
				ps.setString(2, item.getFirstName());
				ps.setString(3, item.getLastName());
				ps.setString(4, item.getEmail());
			}
		});
		
		return jdbcBatchItemWriter;
	}

}
