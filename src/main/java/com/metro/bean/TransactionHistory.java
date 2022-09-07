package com.metro.bean;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionHistory {
	private int id;
	private String sourceMetroStation;
	private String destinationMetroStation;
	private Timestamp swipeInTime;
	private Timestamp swipeOutTime;
	private double fare;
}