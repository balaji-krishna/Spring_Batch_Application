package com.springBatch.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ChunkJobListener implements JobExecutionListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChunkJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("Before Chunk Job name = {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("Before Chunk Job Parameters = {}", jobExecution.getJobParameters());
		LOGGER.info("Before Chunk Job Execution Context = {}", jobExecution.getExecutionContext());

		jobExecution.getExecutionContext().put("Chunk_Job_Execution_Context", "Chunk Job execution context value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("After Chunk Job name = {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("After Chunk Job Parameters = {}", jobExecution.getJobParameters());
		LOGGER.info("After Chunk Job Execution Context = {}", jobExecution.getExecutionContext());
	}

}
