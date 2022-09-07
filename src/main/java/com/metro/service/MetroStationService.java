package com.metro.service;

import org.springframework.http.ResponseEntity;

import com.metro.bean.MetroStation;
import com.metro.bean.MetroStations;

public interface MetroStationService {
	double calculateFare(MetroStation source, MetroStation destination);
	
	ResponseEntity<MetroStation> getMetroStation(int metroStationId);
	
	ResponseEntity<MetroStations> getMetroStations();
}
