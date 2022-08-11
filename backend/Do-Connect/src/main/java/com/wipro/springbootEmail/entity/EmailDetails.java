package com.wipro.springbootEmail.entity;

//Java Program to Illustrate EmailDetails Class



//Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

//Class
public class EmailDetails {

	// Class data members
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	public String getRecipient() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getMsgBody() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getAttachment() {
		// TODO Auto-generated method stub
		return null;
	}
}
