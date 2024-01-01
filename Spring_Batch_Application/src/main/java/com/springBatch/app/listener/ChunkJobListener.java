package com.springBatch.app.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ChunkJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Before Chunk Job name = " + jobExecution.getJobInstance().getJobName());
		System.out.println("Chunk Job Parameters = " + jobExecution.getJobParameters());
		System.out.println("Chunk Job Execution Context = " + jobExecution.getExecutionContext());

		jobExecution.getExecutionContext().put("Chunk_Job_Execution_Context", "Chunk Job execution context value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("After Chunk Job name = " + jobExecution.getJobInstance().getJobName());
		System.out.println("Chunk Job Parameters = " + jobExecution.getJobParameters());
		System.out.println("Chunk Job Execution Context = " + jobExecution.getExecutionContext());
	}

}
