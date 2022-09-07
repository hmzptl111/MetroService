package com.metro.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.metro.bean.Card;
import com.metro.bean.User;
import com.metro.service.CardService;
import com.metro.service.UserService;

@Controller
public class CardController {
	@Autowired
	CardService cardService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	
	
//	======================NEW CARD======================
	@RequestMapping(value = "/newCard", method = RequestMethod.GET)
	public ModelAndView newCardGET(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(session.getAttribute("card") != null) {
			modelAndView.setViewName("index");
		} else {
			modelAndView.setViewName("newCard");
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/newCard", method = RequestMethod.POST)
	public ModelAndView newCardPOST(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("balance") double balance) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			ResponseEntity<User> user = userService.getUserByEmail(email);
			if(user.getStatusCode() == HttpStatus.FOUND) {
				if(cardService.generateCard(new Card(-1, email, password, balance))) {
					modelAndView.setViewName("redirect:/signIn");
				} else {
					modelAndView.addObject("message", "Couldn't generate card");
					modelAndView.setViewName("newCard");
				}
			} else {
				modelAndView.addObject("message", "You must first sign up as a user in order to generate a card");
				modelAndView.setViewName("newCard");
			}
		} catch(HttpClientErrorException e) {
			modelAndView.addObject("message", "Something went wrong");
			modelAndView.setViewName("newCard");
			
			return modelAndView;
		}
		
		return modelAndView;
	}
//	======================NEW CARD======================
	
//	======================CHECK BALANCE======================
	@RequestMapping(value = "/checkBalance", method = RequestMethod.GET)
	public ModelAndView checkBalance(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Card card = (Card)session.getAttribute("card");
		
		if(card == null) {
			modelAndView.addObject("message", "Couldn't process card");
			modelAndView.setViewName("message");
		} else {
			try {
				double balance = cardService.checkBalance(card.getId());

				modelAndView.addObject("balance", balance);
				modelAndView.setViewName("balance");
			} catch(HttpClientErrorException e) {
				modelAndView.addObject("message", "Something went wrong");
				modelAndView.setViewName("balance");
				
				return modelAndView;
			}
		}
		
		return modelAndView;
	}
//	======================CHECK BALANCE======================
	
	
//	======================UPDATE BALANCE======================
	@RequestMapping(value = "/updateBalance", method = RequestMethod.GET)
	public ModelAndView updateBalanceGET(HttpSession session) {
		return new ModelAndView("updateBalance");
	}
	
	@RequestMapping(value = "/updateBalance", method = RequestMethod.POST)
	public ModelAndView updateBalancePOST(@RequestParam("amount") double amount, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Card card = (Card)session.getAttribute("card");
		
		if(card == null) {
			modelAndView.addObject("message", "Couldn't process card");
			modelAndView.setViewName("message");
		} else {
			try {
				boolean isCardBalanceUpdated = cardService.updateBalance(card.getId(), amount);
				String message = isCardBalanceUpdated ? "Card balance updated": "Couldn't update card balance";
				
				modelAndView.addObject("message", message);
				modelAndView.setViewName("updateBalance");
			} catch(HttpClientErrorException e) {
				modelAndView.addObject("message", "Something went wrong");
				modelAndView.setViewName("updateBalance");
				
				return modelAndView;
			}
		}
		
		return modelAndView;
	}
//	======================UPDATE BALANCE======================
}