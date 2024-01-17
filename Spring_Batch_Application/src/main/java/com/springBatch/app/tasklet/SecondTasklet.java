package com.springBatch.app.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class SecondTasklet implements Tasklet {

	public static final Logger LOGGER = LoggerFactory.getLogger(SecondTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOGGER.info("This is the second tasklet step");
		LOGGER.info("Second tasklet step context job execution context = {}",
				chunkContext.getStepContext().getJobExecutionContext());
		return RepeatStatus.FINISHED;
	}

}