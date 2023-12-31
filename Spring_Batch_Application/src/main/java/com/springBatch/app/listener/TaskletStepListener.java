package com.springBatch.app.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Before step name = " + stepExecution.getStepName());
		System.out.println("Before Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("Before Step Execution Context =  " + stepExecution.getExecutionContext());

		stepExecution.getExecutionContext().put("Step_Execution_Context", "Step execution context value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("After step name = " + stepExecution.getStepName());
		System.out.println("After Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("After Step Execution Context = " + stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

}