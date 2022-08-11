package com.wipro.springbootEmail.service;
//Java Program to Illustrate Creation Of
//Service Interface



import com.wipro.springbootEmail.entity.EmailDetails;

//Interface
public interface EmailService {

	// Method
	// To send a simple email
	String sendSimpleMail(EmailDetails details);

	// Method
	// To send an email with attachment
	String sendMailWithAttachment(EmailDetails details);
}
