package com.link.ram.it.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.link.ram.it.dao.IUserDao;
import com.link.ram.it.service.IUserService;
import com.link.ram.it.service.po.User;

/**
 * Demo Service
 * @author dell
 *
 */
@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private IUserDao userDao;
	
	@Override
	public User findUser(Integer id) {
		return userDao.findUser(id);
	}

}
