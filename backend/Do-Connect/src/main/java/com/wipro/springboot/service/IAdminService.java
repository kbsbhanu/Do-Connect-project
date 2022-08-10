package com.wipro.springboot.service;

import java.util.List;

import com.wipro.springboot.dto.AnswerDto;
import com.wipro.springboot.dto.AuthenticationRequest;
import com.wipro.springboot.dto.QuestionDto;
import com.wipro.springboot.dto.SignupRequest;
import com.wipro.springboot.dto.UserDto;
import com.wipro.springboot.response.GeneralResponse;

public interface IAdminService {

	UserDto createAdmin(SignupRequest signupRequest);

	Boolean hasUserWithEmail(String email);

	GeneralResponse login(AuthenticationRequest authenticationRequest);

	List<QuestionDto> getAllQuestions();

	GeneralResponse approveAnswer(Long id);

	GeneralResponse approveQuestion(Long id);

	public void deleteQuestion(Long id);

	public void deleteAnswer(Long id);

	List<AnswerDto> getAllAnswers();
}
