package com.wipro.springboot.service;

import java.util.List;

import javax.validation.Valid;

import com.wipro.springboot.dto.MessageDTO;

public interface IMessageService {

	public MessageDTO sendMessage(@Valid MessageDTO messageDTO);

	public List<MessageDTO> getMessage();

}
