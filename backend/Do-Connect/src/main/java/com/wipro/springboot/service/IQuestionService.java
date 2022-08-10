package com.wipro.springboot.service;

import java.util.List;

import com.wipro.springboot.dto.AnswerDto;
import com.wipro.springboot.dto.QuestionDto;
import com.wipro.springboot.dto.QuestionSearchResponceDto;
import com.wipro.springboot.dto.SingleQuestionDto;
import com.wipro.springboot.response.GeneralResponse;

public interface IQuestionService {

	String addQuestion(QuestionDto questionDto);

	List<QuestionDto> getAllQuestions();

	SingleQuestionDto getQuestionById(Long id, Long userId);

	QuestionSearchResponceDto searchQuestionByTitle(String title, int pageNum);

	GeneralResponse addAnswer(AnswerDto answerDto);

}
