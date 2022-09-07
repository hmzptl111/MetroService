package com.metro.service;

import org.springframework.http.ResponseEntity;

import com.metro.bean.User;

public interface UserService {
	ResponseEntity<User> getUserByEmail(String email);
	
	boolean addUser(User user);
}
