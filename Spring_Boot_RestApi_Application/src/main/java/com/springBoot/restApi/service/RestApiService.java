package com.springBoot.restApi.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springBoot.restApi.dto.CustomerRequest;
import com.springBoot.restApi.dto.CustomerResponse;

@Service
public class RestApiService {

	public List<CustomerResponse> getCustomers() {
		return Arrays.asList(
				new CustomerResponse(1L, "Balaji", "Krishna", "Balaji@gmail.com"),
				new CustomerResponse(2L, "Harish", "Krishna", "Harish@gmail.com"),
				new CustomerResponse(3L, "Deeraj", "KK", "Deeraj@gmail.com"),
				new CustomerResponse(4L, "Ajay", "KR", "Ajay@gmail.com"),
				new CustomerResponse(5L, "Karuna", "KK", "Karuna@gmail.com"),
				new CustomerResponse(6L, "Kaushik", "T", "Kaushik@gmail.com"),
				new CustomerResponse(7L, "Seeru", "Raj", "Seeru@gmail.com")
				);
	}

	public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
		System.out.println("customerRequest == " + customerRequest.toString());
		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setId(customerRequest.getId());
		customerResponse.setFirstName(customerRequest.getFirstName());
		customerResponse.setLastName(customerRequest.getLastName());
		customerResponse.setEmail(customerRequest.getEmail());
		System.out.println("customerResponse == " + customerResponse.toString());
		return customerResponse;
	}

}
