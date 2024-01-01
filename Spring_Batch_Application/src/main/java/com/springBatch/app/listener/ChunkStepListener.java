package com.springBatch.app.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ChunkStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Before Chunk step name = " + stepExecution.getStepName());
		System.out.println("Before Chunk Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("Before Chunk Step Execution Context =  " + stepExecution.getExecutionContext());

		stepExecution.getExecutionContext().put("Step_Execution_Context", "Step execution context value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("After Chunk step name = " + stepExecution.getStepName());
		System.out.println("After Chunk Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("After Chunk Step Execution Context = " + stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

}
