package com.wipro.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.springboot.dto.AnswerDto;
import com.wipro.springboot.dto.AuthenticationRequest;
import com.wipro.springboot.dto.QuestionDto;
import com.wipro.springboot.dto.SignupRequest;
import com.wipro.springboot.dto.UserDto;
import com.wipro.springboot.entity.Admin;
import com.wipro.springboot.entity.Answer;
import com.wipro.springboot.entity.Questions;
import com.wipro.springboot.entity.Status;
import com.wipro.springboot.repository.IAdminRepository;
import com.wipro.springboot.repository.IAnswerRepository;
import com.wipro.springboot.repository.IQuestionsRepository;
import com.wipro.springboot.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminRepository adminRepository;

	@Autowired
	private IQuestionsRepository questionsRepository;

	@Autowired
	private IAnswerRepository answerRepository;

	public UserDto createAdmin(SignupRequest signupRequest) {
		Admin admin = new Admin();
		admin.setEmail(signupRequest.getEmail());
		admin.setName(signupRequest.getName());
		admin.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));

		admin = adminRepository.save(admin);
		if (admin == null)
			return null;

		return admin.mapAdmintoUserDto();
	}

	public Boolean hasUserWithEmail(String email) {
		return adminRepository.findFirstByEmail(email) != null;
	}

	@Override
	public GeneralResponse login(AuthenticationRequest authenticationRequest) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<Admin> optionalAdmin = adminRepository.findByEmail(authenticationRequest.getUsername());
		if (optionalAdmin.isPresent()) {
			if (new BCryptPasswordEncoder().matches(authenticationRequest.getPassword(),
					optionalAdmin.get().getPassword())) {
				UserDto userDto = new UserDto();
				userDto.setId(optionalAdmin.get().getId());
				userDto.setRole(1);

				generalResponse.setStatus(HttpStatus.OK);
				generalResponse.setData(userDto);
			} else {
				generalResponse.setStatus(HttpStatus.NOT_ACCEPTABLE);
				generalResponse.setMessage("Password is not correct");
			}
		} else {
			generalResponse.setStatus(HttpStatus.NOT_ACCEPTABLE);
			generalResponse.setMessage("Admin Not Found");
		}
		return generalResponse;
	}

	@Override
	public List<QuestionDto> getAllQuestions() {
		List<QuestionDto> questionDtoList = new ArrayList<>();
		questionsRepository.findAllByStatus(Status.POSTED).forEach(questions -> {
			QuestionDto questionDto = questions.getDto();
			questionDto.setUserName(questions.getUser().getName());
			questionDtoList.add(questionDto);
		});
		return questionDtoList;
	}

	@Override
	public List<AnswerDto> getAllAnswers() {
		List<AnswerDto> answerDtoList = new ArrayList<>();
		answerRepository.findAllByStatus(Status.POSTED).forEach(answer -> {
			AnswerDto answerDto = answer.getDto();
			answerDto.setUser_id(answer.getUser().getId());
			answerDto.setUserName(answer.getUser().getName());
			answerDto.setReturnedImg(answer.getImg());

			answerDtoList.add(answerDto);

		});
		return answerDtoList;
	}

	@Override
	public GeneralResponse approveAnswer(Long id) {
		GeneralResponse response = new GeneralResponse();
		Optional<Answer> optionalAnswer = answerRepository.findById(id);
		if (optionalAnswer.isPresent()) {
			Answer answer = optionalAnswer.get();
			answer.setStatus(Status.APPROVED);
			answerRepository.save(answer);

			response.setMessage("Answer Approved Successfully");
			response.setStatus(HttpStatus.OK);
			return response;
		} else {
			response.setMessage("Answer Not Found");
			response.setStatus(HttpStatus.NOT_ACCEPTABLE);
			return response;
		}
	}

	@Override
	public GeneralResponse approveQuestion(Long id) {
		GeneralResponse response = new GeneralResponse();
		Optional<Questions> optionalQuestions = questionsRepository.findById(id);
		if (optionalQuestions.isPresent()) {
			Questions questions = optionalQuestions.get();
			questions.setStatus(Status.APPROVED);
			questionsRepository.save(questions);

			response.setMessage("Question Approved Successfully");
			response.setStatus(HttpStatus.OK);
			return response;
		} else {
			response.setMessage("Answer Not Found");
			response.setStatus(HttpStatus.NOT_ACCEPTABLE);
			return response;
		}

	}

	@Override
	public void deleteQuestion(Long id) {
		questionsRepository.deleteById(id);
	}

	@Override
	public void deleteAnswer(Long id) {
		answerRepository.deleteById(id);
	}
}
