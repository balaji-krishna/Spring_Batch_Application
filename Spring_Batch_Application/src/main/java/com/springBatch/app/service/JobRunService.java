package com.springBatch.app.service;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springBatch.app.dto.JobParametersRequest;

@Service
public class JobRunService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(JobRunService.class);
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Qualifier("mainChunkJob")
	@Autowired
	Job mainChunkJob;
	
	@Qualifier("flatFileChunkJob")
	@Autowired
	Job flatFileChunkJob;
	
	@Qualifier("jsonChunkJob")
	@Autowired
	Job jsonChunkJob;
	
	@Qualifier("xmlChunkJob")
	@Autowired
	Job xmlChunkJob;
	
	@Qualifier("jdbcChunkJob")
	@Autowired
	Job jdbcChunkJob;
	
	@Qualifier("mainTaskletJob")
	@Autowired
	Job mainTaskletJob;
	
	@Qualifier("restCallChunkJob")
	@Autowired
	Job restCallChunkJob;
	
	@Qualifier("jpaChunkJob")
	@Autowired
	Job jpaChunkJob;
	
	@Async
	public void startJob(String jobName, List<JobParametersRequest> jobParamsRequestList) {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		jobParamsRequestList.stream().forEach(jobPramReq -> {
			params.put(jobPramReq.getParamKey(), 
					new JobParameter(jobPramReq.getParamValue()));
		});
		
		JobParameters jobParameters = new JobParameters(params);
		
		try {
			JobExecution jobExecution = null;
			if(jobName.equals("Main_Chunk_Job")) {
				jobExecution = jobLauncher.run(mainChunkJob, jobParameters);
			} else if(jobName.equals("Flat_File_Chunk_Job")) {
				jobExecution = jobLauncher.run(flatFileChunkJob, jobParameters);
			} else if(jobName.equals("Json_Chunk_Job")) {
				jobExecution = jobLauncher.run(jsonChunkJob, jobParameters);
			} else if(jobName.equals("Xml_Chunk_Job")) {
				jobExecution = jobLauncher.run(xmlChunkJob, jobParameters);
			} else if(jobName.equals("JDBC_Chunk_Job")) {
				jobExecution = jobLauncher.run(jdbcChunkJob, jobParameters);
			} else if(jobName.equals("Main_Tasklet_Job")) {
				jobExecution = jobLauncher.run(mainTaskletJob, jobParameters);
			} else if(jobName.equals("Rest_Chunk_Job")) {
				jobExecution = jobLauncher.run(restCallChunkJob, jobParameters);
			} else if(jobName.equals("JPA_Chunk_Job")) {
				jobExecution = jobLauncher.run(jpaChunkJob, jobParameters);
			}
			LOGGER.info("Job Execution ID = {}", jobExecution.getId());
		}catch(Exception e) {
			LOGGER.error("Exception while starting job");
		}
	}

}
