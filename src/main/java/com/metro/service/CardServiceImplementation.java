package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.Card;

@Service
public class CardServiceImplementation implements CardService {
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public boolean generateCard(Card card) throws HttpClientErrorException {
		return restTemplate.postForObject("http://cardService/cards/add", card, Boolean.class);
	}

	@Override
	public double checkBalance(int cardId) throws HttpClientErrorException {
		return restTemplate.getForObject("http://cardService/cards/balance/" + cardId, Double.class);
	}

	@Override
	public boolean updateBalance(int cardId, double amount) throws HttpClientErrorException {
		if(amount < 0) return false;
		
		return restTemplate.getForObject("http://cardService/cards/addBalance/" + cardId + "/" + amount, Boolean.class);
	}

	@Override
	public boolean deductFare(int cardId, double journeyFare) throws HttpClientErrorException {
		return restTemplate.getForObject("http://cardService/cards/deductBalance/" + cardId + "/" + journeyFare, Boolean.class);
	}
}