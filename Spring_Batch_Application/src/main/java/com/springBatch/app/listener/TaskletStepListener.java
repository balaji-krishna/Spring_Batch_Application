package com.springBatch.app.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class TaskletStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Before Tasklet step name = " + stepExecution.getStepName());
		System.out.println("Tasklet Before Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("Tasklet Before Step Execution Context =  " + stepExecution.getExecutionContext());

		stepExecution.getExecutionContext().put("Step_Execution_Context", "Step execution context value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("After Tasklet step name = " + stepExecution.getStepName());
		System.out.println("After Tasklet Job Execution Context = " + stepExecution.getJobExecution().getExecutionContext());
		System.out.println("After Tasklet Step Execution Context = " + stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

}