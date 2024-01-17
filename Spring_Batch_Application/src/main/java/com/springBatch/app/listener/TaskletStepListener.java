package com.springBatch.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletStepListener implements StepExecutionListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(TaskletStepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Before Tasklet step name = {}", stepExecution.getStepName());
		LOGGER.info("Before Tasklet Job Execution Context = {}", stepExecution.getJobExecution().getExecutionContext());
		LOGGER.info("Before Tasklet step Execution Context = {}", stepExecution.getExecutionContext());

		stepExecution.getExecutionContext().put("Step_Execution_Context", "Step execution context value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("After Tasklet step name = {}", stepExecution.getStepName());
		LOGGER.info("After Tasklet Job Execution Context = {}", stepExecution.getJobExecution().getExecutionContext());
		LOGGER.info("After Tasklet step Execution Context = {}", stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

}