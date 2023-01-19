package com.eventapp.repository;

import org.springframework.data.repository.CrudRepository;
import com.eventapp.models.Event;

public interface EventRepository extends CrudRepository<Event, String>{
	Event findByCode(long code);
}
