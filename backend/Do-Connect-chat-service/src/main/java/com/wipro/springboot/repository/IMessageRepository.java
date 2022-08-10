package com.wipro.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.springboot.entity.Message;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

}
