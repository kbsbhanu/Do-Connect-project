package com.wipro.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.springboot.entity.Answer;
import com.wipro.springboot.entity.Status;

import java.util.List;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {

	List<Answer> findAllByStatus(Status status);
}
