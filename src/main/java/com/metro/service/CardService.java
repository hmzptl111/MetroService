package com.metro.service;

import com.metro.bean.Card;

public interface CardService {
	double MINIMUM_BALANCE = 20;
	
	boolean generateCard(Card card);
	
	double checkBalance(int cardId);
	
	boolean updateBalance(int cardId, double amount);

	boolean deductFare(int cardId, double journeyFare);
}