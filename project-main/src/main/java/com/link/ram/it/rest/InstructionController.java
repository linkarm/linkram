package com.link.ram.it.rest;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.link.ram.socket.SocketServer;

@RestController
@RequestMapping("/instruct")
public class InstructionController {

	@GetMapping("/send/{msg}")
	public String helloworld(@PathVariable String msg) {
		try {
			SocketServer.getSocketServerInstance().sendMessageToAll(msg);
			return "success to send message";
		} catch (IOException e) {
			e.printStackTrace();
			return "failed to send message";
		}
	}
}
