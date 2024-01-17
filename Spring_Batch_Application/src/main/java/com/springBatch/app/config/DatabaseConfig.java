package com.springBatch.app.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DatabaseConfig {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource datasource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.newcustomerdatasource")
	public DataSource newcustomerdatasource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Primary
	public EntityManagerFactory mysqlEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lem = 
				new LocalContainerEntityManagerFactoryBean();
		
		lem.setDataSource(datasource());
		lem.setPackagesToScan("com.springBatch.app.entity");
		lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lem.afterPropertiesSet();
		
		return lem.getObject();
	}
	
	@Bean
	public EntityManagerFactory newmysqlEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lem = 
				new LocalContainerEntityManagerFactoryBean();
		
		lem.setDataSource(newcustomerdatasource());
		lem.setPackagesToScan("com.springBatch.app.entity");
		lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lem.afterPropertiesSet();
		
		return lem.getObject();
	}
	
	@Bean
	@Primary
	public JpaTransactionManager jpaTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new 
				JpaTransactionManager();
		
		jpaTransactionManager.setDataSource(newcustomerdatasource());
		jpaTransactionManager.setEntityManagerFactory(newmysqlEntityManagerFactory());
		
		return jpaTransactionManager;
	}
}
