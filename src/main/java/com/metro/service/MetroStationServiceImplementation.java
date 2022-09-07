package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.MetroStation;
import com.metro.bean.MetroStations;

@Service
public class MetroStationServiceImplementation implements MetroStationService {
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public double calculateFare(MetroStation source, MetroStation destination) {
		int factor = Math.abs(source.getId() - destination.getId());
		double fair = factor * 5;
		
		return fair;
	}

	@Override
	public ResponseEntity<MetroStation> getMetroStation(int metroStationId) throws HttpClientErrorException {
		return restTemplate.getForEntity("http://metroStationService/stations/" + metroStationId, MetroStation.class);
	}

	@Override
	public ResponseEntity<MetroStations> getMetroStations() {
		return restTemplate.getForEntity("http://metroStationService/stations", MetroStations.class);
	}
}