package com.springBatch.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springBatch.app.listener.TaskletJobListener;
import com.springBatch.app.listener.TaskletStepListener;
import com.springBatch.app.tasklet.FirstTasklet;
import com.springBatch.app.tasklet.SecondTasklet;

@Configuration
public class TaskletJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private FirstTasklet firstTasklet;

	@Autowired
	private SecondTasklet secondTasklet;

	@Autowired
	private TaskletJobListener taskletJobListener;

	@Autowired
	private TaskletStepListener taskletStepListener;

	@Bean
	public Job mainTaskletJob() {
		return jobBuilderFactory.get("Main Tasklet Job")
				.incrementer(new RunIdIncrementer())
				.start(firstTaskletStep())
				.next(secondTaskletStep())
				.listener(taskletJobListener)
				.build();
	}

	@Bean
	public Step firstTaskletStep() {
		return stepBuilderFactory.get("First Tasklet Step")
				.tasklet(firstTasklet)
				.listener(taskletStepListener)
				.build();
	}

	@Bean
	public Step secondTaskletStep() {
		return stepBuilderFactory.get("Second Tasklet Step")
				.tasklet(secondTasklet)
				.listener(taskletStepListener)
				.build();
	}

}