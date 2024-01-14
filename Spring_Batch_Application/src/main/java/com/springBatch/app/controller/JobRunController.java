package com.springBatch.app.controller;

import java.util.List;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springBatch.app.entity.JobParametersRequest;
import com.springBatch.app.service.JobRunService;

@RestController
@RequestMapping(path = "/springbatch")
public class JobRunController {
	
	@Autowired
	private JobRunService jobRunService;
	
	@Autowired
	JobOperator jobOperator;
	
	@GetMapping("/startJob/{jobName}")
	public String startJob(@PathVariable String jobName, 
			@RequestBody List<JobParametersRequest> jobParamsRequestList) throws Exception {
		jobRunService.startJob(jobName, jobParamsRequestList);
		return "The requested job is started";
	}
	
	@GetMapping("/stop/{jobExecutionId}")
	public String stopJob(@PathVariable long jobExecutionId) {
		try {
			jobOperator.stop(jobExecutionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "The requested job is stopped";
	}

}
