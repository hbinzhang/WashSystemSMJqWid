package com.hry.dispatch.service;

import java.util.List;

import com.hry.dispatch.domain.User;

public interface UserServiceI {

	/**
	 * ����û�
	 * 
	 * @param user
	 */
	public void addUser(User user);

	/**
	 * �����û�id��ȡ�û�
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId);
	
	public List<User> getAllUser();
	
	public User auth(String userName, String password);
}