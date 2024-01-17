package com.springBatch.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ChunkStepListener implements StepExecutionListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChunkStepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Before Chunk step name = {}", stepExecution.getStepName());
		LOGGER.info("Before Chunk step Job Execution Context = {}",
				stepExecution.getJobExecution().getExecutionContext());
		LOGGER.info("Before Chunk step Execution Context = {}", stepExecution.getExecutionContext());

		stepExecution.getExecutionContext().put("Step_Execution_Context", "Step execution context value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("After Chunk step name = {}", stepExecution.getStepName());
		LOGGER.info("After Chunk step Job Execution Context = {}",
				stepExecution.getJobExecution().getExecutionContext());
		LOGGER.info("After Chunk step Execution Context = {}", stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

}
