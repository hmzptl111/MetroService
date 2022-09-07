package com.metro.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.metro.bean.Transaction;
import com.metro.bean.TransactionHistory;

public interface TransactionService {
	boolean addTransaction(int cardId, int sourceMetroStationId);

	boolean updateTransaction(int transactionId, int destinationMetroStationId, double fare);

	ResponseEntity<Transaction> getLastTransaction(int cardId);
	
	List<TransactionHistory> getTransactionHistory(int cardId);
}
