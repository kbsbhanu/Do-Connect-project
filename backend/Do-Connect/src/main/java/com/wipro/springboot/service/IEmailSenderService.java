package com.wipro.springboot.service;

import org.springframework.stereotype.Service;

@Service
public interface IEmailSenderService {

	String sendQueEmail();
	
	String sendAnsEmail();
}
