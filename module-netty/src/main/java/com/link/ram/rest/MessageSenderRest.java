package com.link.ram.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.link.ram.service.IMessageSender;

@RestController
@RequestMapping("/sender")
public class MessageSenderRest {
	
	@Autowired
	private IMessageSender messageSender;
	
	@GetMapping("/send/{msg}")
	public String helloworld(@PathVariable String msg) {
		messageSender.sendMessageToAll(msg);
		return "success";
	}
}
