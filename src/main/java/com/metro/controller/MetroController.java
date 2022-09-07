package com.metro.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.metro.bean.Card;
import com.metro.bean.Transaction;
import com.metro.service.TransactionService;

@RestController
public class MetroController {
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/")
	public ModelAndView getIndex(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Card card = (Card)session.getAttribute("card");
		if(card == null) {
			modelAndView.setViewName("redirect:/signIn");
		} else {
			try {
				ResponseEntity<Transaction> lastTransaction = transactionService.getLastTransaction(card.getId());
				if(lastTransaction.getStatusCode() == HttpStatus.FOUND) {
					Timestamp lastTransactionSwipeInTime = lastTransaction.getBody().getSwipeInTime();
					Timestamp lastTransactionSwipeOutTime = lastTransaction.getBody().getSwipeOutTime();
					if(lastTransactionSwipeInTime != null && lastTransactionSwipeOutTime == null) {
						session.setAttribute("isSwipedIn", "true");
					} else {
						session.setAttribute("isSwipedIn", "false");
					}
				}
				modelAndView.setViewName("index");
			} catch(HttpClientErrorException e) {
				modelAndView.addObject("message", "Something went wrong");
				modelAndView.setViewName("signIn");
				
				return modelAndView;
			}
		}
		
		return modelAndView;
	}
}