package com.eventapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventapp.models.Event;
import com.eventapp.models.Guest;

public interface GuestRepository extends CrudRepository<Guest, String>{
	Iterable<Guest> findByEvent(Event event);
	Guest findByRg(String rg);
}
