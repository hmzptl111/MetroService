package com.metro.service;

import org.springframework.http.ResponseEntity;

import com.metro.bean.Card;
import com.metro.bean.User;

public interface AuthService {
	ResponseEntity<Card> signIn(String email, String password);
	
	ResponseEntity<User> signUp(User user);
}
