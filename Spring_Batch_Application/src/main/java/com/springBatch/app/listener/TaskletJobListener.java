package com.springBatch.app.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Before Tasklet Job name = " + jobExecution.getJobInstance().getJobName());
		System.out.println("Tasklet Job Parameters = " + jobExecution.getJobParameters());
		System.out.println("Tasklet Job Execution Context = " + jobExecution.getExecutionContext());

		jobExecution.getExecutionContext().put("Tasklet_Job_Execution_Context", "Tasklet Job execution context value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("After Tasklet Job name = " + jobExecution.getJobInstance().getJobName());
		System.out.println("Tasklet Job Parameters = " + jobExecution.getJobParameters());
		System.out.println("Tasklet Job Execution Context = " + jobExecution.getExecutionContext());
	}

}