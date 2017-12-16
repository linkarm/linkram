package com.link.ram.it.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.link.ram.it.service.IUserService;
import com.link.ram.it.service.po.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userSerivce;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public User findUsers(@PathVariable Integer id){
		return userSerivce.findUser(id);
	}
}
