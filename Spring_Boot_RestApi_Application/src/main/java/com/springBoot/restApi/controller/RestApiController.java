package com.springBoot.restApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.restApi.dto.CustomerRequest;
import com.springBoot.restApi.dto.CustomerResponse;
import com.springBoot.restApi.service.RestApiService;

@RestController
@RequestMapping(path = "restApi")
public class RestApiController {
	
	@Autowired
	private RestApiService restApiService;
	
	@GetMapping(path = "getCustomers")
	public List<CustomerResponse> getCustomers(){
		return restApiService.getCustomers();
	}
	
	@PostMapping(path = "saveCustomer")
	public CustomerResponse saveCustomer(@RequestBody CustomerRequest customerRequest) {
		return restApiService.saveCustomer(customerRequest);
	}

}
