package com.springBatch.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springBatch.app.entity.CustomerCsv;
import com.springBatch.app.entity.CustomerResponse;

@Service
public class RestCallService {
	
	List<CustomerResponse> list;
	
	public List<CustomerResponse> restCallToGetCustomers() {
		RestTemplate restTemplate = new RestTemplate();
		CustomerResponse[] customerResponseArray = restTemplate.getForObject("http://localhost:9090/restApi/getCustomers",
				CustomerResponse[].class);

		list = new ArrayList<>();

		for (CustomerResponse customerResponse : customerResponseArray) {
			list.add(customerResponse);
		}

		return list;
	}
	
	public CustomerResponse getCustomers() {
		if (list == null) {
			restCallToGetCustomers();
		}

		if (list != null && !list.isEmpty()) {
			return list.remove(0);
		}
		return null;
	}
	
	public CustomerResponse restCallToCreateCustomer(CustomerCsv customerCsv) {
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.postForObject("http://localhost:9090/restApi/saveCustomer", 
				customerCsv, 
				CustomerResponse.class);
	}

}
