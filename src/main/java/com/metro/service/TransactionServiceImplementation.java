package com.metro.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.MetroStation;
import com.metro.bean.Transaction;
import com.metro.bean.TransactionHistory;
import com.metro.bean.Transactions;

@Service
public class TransactionServiceImplementation implements TransactionService {
	@Autowired
	RestTemplate restTemplate;

	@Override
	public boolean addTransaction(int cardId, int sourceMetroStationId) {
		return restTemplate.getForObject("http://transactionService/transactions/swipein/" + cardId + "/" + sourceMetroStationId, Boolean.class);
	}

	@Override
	public boolean updateTransaction(int transactionId, int destinationMetroStationId, double fare) throws HttpClientErrorException {
		return restTemplate.getForObject("http://transactionService/transactions/swipeout/" + transactionId + "/" + destinationMetroStationId + "/" + fare, Boolean.class);
	}
	
	@Override
	public ResponseEntity<Transaction> getLastTransaction(int cardId) throws HttpClientErrorException {
		return restTemplate.getForEntity("http://transactionService/transactions/last/" + cardId, Transaction.class);
	}

	@Override
	public List<TransactionHistory> getTransactionHistory(int cardId) throws HttpClientErrorException {
		List<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
		ResponseEntity<Transactions> transactions = restTemplate.getForEntity("http://transactionService/transactions/" + cardId, Transactions.class);
		
		if(transactions.getStatusCode() == HttpStatus.OK) {
			for(Transaction transaction: transactions.getBody().getTransactions()) {
				ResponseEntity<MetroStation> sourceMetroStation = restTemplate.getForEntity("http://metroStationService/stations/" + transaction.getSourceId(), MetroStation.class);
				
				boolean isSwipedOut = transaction.getDestinationId() != null;
				ResponseEntity<MetroStation> destinationMetroStation = null;
				if(isSwipedOut) {
					destinationMetroStation = restTemplate.getForEntity("http://metroStationService/stations/" + transaction.getDestinationId(), MetroStation.class);
				}
				
				TransactionHistory th = new TransactionHistory(transaction.getId(), "Unavailable", "Unavailable", transaction.getSwipeInTime(), Timestamp.from(Instant.EPOCH), 0.00);
				if(sourceMetroStation.getStatusCode() == HttpStatus.FOUND) {
					th.setSourceMetroStation(sourceMetroStation.getBody().getName());
				}
				if(isSwipedOut && destinationMetroStation.getStatusCode() == HttpStatus.FOUND) {
					th.setDestinationMetroStation(destinationMetroStation.getBody().getName());
					th.setSwipeOutTime(transaction.getSwipeOutTime());
					th.setFare(transaction.getFare());
				}
				transactionHistory.add(th);
			}
		}
		
		return transactionHistory;
	}
}