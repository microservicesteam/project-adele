package com.microservicesteam.adele.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.model.Event;

@Service
public class BookingServiceImpl implements BookingService {

	@Override
	public List<Event> getEvents() {
		return ImmutableList.of();
	}

}
