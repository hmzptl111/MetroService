package com.metro.bean;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {
	private Integer id;
	private Integer cardId;
	private Integer sourceId;
	private Integer destinationId;
	private Timestamp swipeInTime;
	private Timestamp swipeOutTime;
	private Double fare;
}
