package com.link.ram.it.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorld {
	
	@GetMapping("/world/{name}")
	public String helloworld(@PathVariable String name) {
		return name+ ",hello world";
	}
}
