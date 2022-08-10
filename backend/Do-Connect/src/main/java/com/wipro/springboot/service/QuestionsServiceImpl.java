package com.wipro.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wipro.springboot.dto.AnswerDto;
import com.wipro.springboot.dto.QuestionDto;
import com.wipro.springboot.dto.QuestionSearchResponceDto;
import com.wipro.springboot.dto.SingleQuestionDto;
import com.wipro.springboot.entity.Answer;
import com.wipro.springboot.entity.Questions;
import com.wipro.springboot.entity.Status;
import com.wipro.springboot.entity.User;
import com.wipro.springboot.repository.IAnswerRepository;
import com.wipro.springboot.repository.IQuestionsRepository;
import com.wipro.springboot.repository.IUserRepository;
import com.wipro.springboot.response.GeneralResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServiceImpl implements IQuestionService {

	@Autowired
	private IQuestionsRepository questionsRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IAnswerRepository answerRepository;

	public static final int SEARCH_RESULT_PER_PAGE = 5;

	@Override
	public String addQuestion(QuestionDto questionDto) {
		User user = null;
		Optional<User> userOptional = userRepository.findById(questionDto.getUser_id());
		if (userOptional.isPresent()) {
			user = userOptional.get();

			Questions questions = new Questions();
			questions.setCreatedDate(new Date());

			questions.setTitle(questionDto.getTitle());
			questions.setBody(questionDto.getBody());
			questions.setCreatedDate(new Date());
			questions.setStatus(Status.POSTED);
			questions.setUser(user);
			questionsRepository.save(questions);
			return "Questions Added Successfully";
		} else {
			return "User Not Found";
		}
	}

	@Override
	public List<QuestionDto> getAllQuestions() {
		List<QuestionDto> questionDtoList = new ArrayList<>();
		questionsRepository.findAll().forEach(questions -> {
			QuestionDto questionDto = questions.getDto();
			questionDto.setUserName(questions.getUser().getName());
			questionDtoList.add(questionDto);
		});
		return questionDtoList;
	}

	@Override
	public SingleQuestionDto getQuestionById(Long id, Long userId) {
		SingleQuestionDto singleQuestionDto = new SingleQuestionDto();
		Questions questions = null;
		Optional<Questions> optionalQuestions = questionsRepository.findById(id);
		if (optionalQuestions.isPresent()) {
			questions = optionalQuestions.get();
			QuestionDto questionDto = questions.getDto();
			questionDto.setUserName(questions.getUser().getName());
			questionDto.setUser_id(questions.getUser().getId());
			questionDto.setVoted(0);

			singleQuestionDto.setQuestionDto(questionDto);
			List<AnswerDto> answerDtos = new ArrayList<>();
			questions.getAnswerList().forEach(answer -> {
				if (answer.getStatus() == Status.APPROVED) {
					AnswerDto answerDto = answer.getDto();
					answerDto.setUser_id(answer.getUser().getId());
					answerDto.setUserName(answer.getUser().getName());
					answerDto.setReturnedImg(answer.getImg());
					if (answer.getUser().getId().equals(userId)) {
						answerDto.setUserName("You ");
					}

					answerDtos.add(answerDto);
				}
			});
			singleQuestionDto.setAnswerDtoList(answerDtos);

		}
		return singleQuestionDto;
	}

	@Override
	public GeneralResponse addAnswer(AnswerDto answerDto) {
		GeneralResponse response = new GeneralResponse();
		User user = null;
		Questions questions = null;
		Optional<User> userOptional = userRepository.findById(answerDto.getUser_id());
		Optional<Questions> optionalQuestions = questionsRepository.findById(answerDto.getQuestion_id());
		try {
			if (userOptional.isPresent() && optionalQuestions.isPresent()) {
				user = userOptional.get();
				questions = optionalQuestions.get();

				Answer answer = new Answer();
				answer.setStatus(Status.POSTED);
				answer.setBody(answerDto.getBody());
				answer.setCreatedDate(new Date());
				answer.setQuestions(questions);
				if (answerDto.getImg() != null) {
					answer.setImg(answerDto.getImg().getBytes());
				}

				answer.setUser(user);

				response.setData(answerRepository.save(answer).getId());
				response.setMessage("Answer Added Successfully");
				response.setStatus(HttpStatus.OK);
				return response;
			} else {
				response.setMessage("Some Attribute Not Found");
				response.setStatus(HttpStatus.NOT_ACCEPTABLE);
				return response;
			}
		} catch (Exception e) {
			response.setMessage("Failed to save img.");
			response.setStatus(HttpStatus.NOT_ACCEPTABLE);
			return response;
		}
	}

	@Override
	public QuestionSearchResponceDto searchQuestionByTitle(String title, int pageNum) {

		Pageable paging = PageRequest.of(pageNum, SEARCH_RESULT_PER_PAGE);

		Page<Questions> questionsPage;
		if (title == null || title.equals("null"))
			questionsPage = questionsRepository.findAllByStatus(paging, Status.APPROVED);
		else
			questionsPage = questionsRepository.findAllByTitleContainingAndStatus(title, paging, Status.APPROVED);

		QuestionSearchResponceDto questionSearchResponceDto = new QuestionSearchResponceDto();
		questionSearchResponceDto.setQuestionDtoList(getQuestionListDto(questionsPage));
		questionSearchResponceDto.setPageNumber(questionsPage.getPageable().getPageNumber());
		questionSearchResponceDto.setTotalPages(questionsPage.getTotalPages());
		return questionSearchResponceDto;
	}

	List<QuestionDto> getQuestionListDto(Page<Questions> questionsPage) {
		List<QuestionDto> questionDtoList = new ArrayList<>();
		questionsPage.getContent().forEach(question -> {
			QuestionDto questionDto = question.getDto();
			questionDto.setUserName(question.getUser().getName());
			questionDtoList.add(questionDto);
		});
		return questionDtoList;
	}

}
