package com.link.ram.it.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instruct")
public class InstructionController {

	@GetMapping("/send/{msg}")
	public String helloworld(@PathVariable String msg) {
		return null;
	}
}
