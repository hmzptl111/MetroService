package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.User;

@Service
public class UserServiceImplementation implements UserService {
	@Autowired
	RestTemplate restTemplate;

	@Override
	public ResponseEntity<User> getUserByEmail(String email) throws HttpClientErrorException {
		return restTemplate.getForEntity("http://userService/users/" + email, User.class);
	}

	@Override
	public boolean addUser(User user) throws HttpClientErrorException {
		ResponseEntity<User> newUser = restTemplate.postForEntity("http://userService/users/add", user, User.class);
		return newUser.getStatusCode() == HttpStatus.CREATED;
	}
}