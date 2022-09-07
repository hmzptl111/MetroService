package com.metro.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.metro.bean.Card;
import com.metro.bean.Transaction;
import com.metro.bean.User;
import com.metro.service.AuthService;
import com.metro.service.TransactionService;

@RestController
public class AuthController {
	@Autowired
	private AuthService authService;
	
	@Autowired
	private TransactionService transactionService;
	
	
//	======================SIGN IN======================
	@GetMapping(path = "/signIn")
	public ModelAndView signInGET(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(session.getAttribute("card") == null) {
			modelAndView.setViewName("signIn");
		} else {
			modelAndView.setViewName("index");
		}
		return modelAndView;
	}
	
	@PostMapping(path = "/signIn")
	public ModelAndView signInPOST(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		ResponseEntity<Card> card = null;
		try {
			card = authService.signIn(email, password);
			session.setAttribute("card", card.getBody());
			try {
				ResponseEntity<Transaction> lastTransaction = transactionService.getLastTransaction(card.getBody().getId());
				if(lastTransaction.getStatusCode() == HttpStatus.FOUND) {
					Timestamp lastTransactionSwipeInTime = lastTransaction.getBody().getSwipeInTime();
					Timestamp lastTransactionSwipeOutTime = lastTransaction.getBody().getSwipeOutTime();
					if(lastTransactionSwipeInTime != null && lastTransactionSwipeOutTime == null) {
						session.setAttribute("isSwipedIn", "true");
					} else {
						session.setAttribute("isSwipedIn", "false");
					}
				}
			} catch(HttpClientErrorException e) {
				session.setAttribute("isSwipedIn", "false");
				modelAndView.setViewName("index");
				
				return modelAndView;
			}
			modelAndView.setViewName("index");
		} catch(HttpClientErrorException e) {
			if(card != null && card.getStatusCode() == HttpStatus.NOT_FOUND) {
				modelAndView.addObject("message", "Incorrect credentials");
			} else {
				modelAndView.addObject("message", "Something went wrong");
			}
			modelAndView.setViewName("signIn");
			
			return modelAndView;
		}
		
		return modelAndView;
	}
//	======================SIGN IN======================
	
	
//	======================SIGN OUT======================
	@RequestMapping(value = "/signOut", method = RequestMethod.POST)
	public ModelAndView signOut(HttpSession session) {
		session.invalidate();
		
		return new ModelAndView("redirect:/signIn");
	}
//	======================SIGN OUT======================
	
//	======================SIGN UP======================
	@GetMapping(path = "/signUp")
	public ModelAndView signUpGET(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(session.getAttribute("card") == null) {
			modelAndView.setViewName("signUp");
		} else {
			modelAndView.setViewName("index");
		}
		return modelAndView;
	}
	
	@PostMapping(path = "/signUp")
	public ModelAndView signUpPOST(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("contact") long contact, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(session.getAttribute("card") == null) {
			ResponseEntity<User> user = null;
			try {
				user = authService.signUp(new User(email, name, contact));
				if(user.getStatusCode() == HttpStatus.CREATED) {
					modelAndView.setViewName("redirect:/newCard");
				} else {
					modelAndView.addObject("message", "Couldn't sign up");
					modelAndView.setViewName("signUp");
				}
			} catch(HttpClientErrorException e) {
				if(user != null && user.getStatusCode() == HttpStatus.BAD_REQUEST) {
					modelAndView.addObject("message", "Incorrect credentials");
				} else {
					modelAndView.addObject("message", "Something went wrong");
				}
				modelAndView.setViewName("signUp");
				
				return modelAndView;
			}
		} else {
			modelAndView.setViewName("redirect:/");
		}
		
		return modelAndView;
	}
//	======================SIGN UP======================
}
