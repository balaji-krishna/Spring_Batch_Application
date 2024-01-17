package com.springBatch.app.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FirstTasklet implements Tasklet {

	public static final Logger LOGGER = LoggerFactory.getLogger(FirstTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOGGER.info("This is the first tasklet step");
		LOGGER.info("First tasklet step context job execution context = {}",
				chunkContext.getStepContext().getJobExecutionContext());
		return RepeatStatus.FINISHED;
	}

}