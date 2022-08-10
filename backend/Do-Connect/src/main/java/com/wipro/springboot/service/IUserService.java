package com.wipro.springboot.service;

import com.wipro.springboot.dto.SignupRequest;
import com.wipro.springboot.dto.UserDto;

public interface IUserService {

	void createAdminAccount();

	UserDto createUser(SignupRequest signupRequest);

	Boolean hasUserWithEmail(String email);
}
