package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.Card;
import com.metro.bean.User;

@Service
public class AuthServiceImplementation implements AuthService {
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ResponseEntity<Card> signIn(String email, String password) throws HttpClientErrorException {
		return restTemplate.getForEntity("http://cardService/cards/" + email + "/" + password, Card.class);
	}

	@Override
	public ResponseEntity<User> signUp(User user) throws HttpClientErrorException {
		return restTemplate.postForEntity("http://userService/users/add", user, User.class);
	}
}