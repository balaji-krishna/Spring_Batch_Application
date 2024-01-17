package com.springBatch.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletJobListener implements JobExecutionListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(TaskletJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("Before Tasklet Job name = {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("Before Tasklet Job Parameters = {}", jobExecution.getJobParameters());
		LOGGER.info("Before Tasklet Job Execution Context = {}", jobExecution.getExecutionContext());

		jobExecution.getExecutionContext().put("Tasklet_Job_Execution_Context", "Tasklet Job execution context value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("After Tasklet Job name = {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("After Tasklet Job Parameters = {}", jobExecution.getJobParameters());
		LOGGER.info("After Tasklet Job Execution Context = {}", jobExecution.getExecutionContext());
	}

}