package com.springBatch.app.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MainChunkJobScheduler {

	public static final Logger LOGGER = LoggerFactory.getLogger(MainChunkJobScheduler.class);

	@Autowired
	JobLauncher jobLauncher;

	@Qualifier("mainChunkJob")
	@Autowired
	Job mainChunkJob;

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void secondJobStarter() {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));

		JobParameters jobParameters = new JobParameters(params);

		try {
			JobExecution jobExecution = jobLauncher.run(mainChunkJob, jobParameters);
			LOGGER.info("Job Execution ID = {}", jobExecution.getId());
		} catch (Exception e) {
			LOGGER.error("Exception while starting job");
		}
	}

}
