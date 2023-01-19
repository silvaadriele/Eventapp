package com.eventapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventapp.models.Event;
import com.eventapp.models.Guest;
import com.eventapp.repository.EventRepository;
import com.eventapp.repository.GuestRepository;

import jakarta.validation.Valid;

@Controller
public class EventController {
	
	@Autowired
	private EventRepository er;
	@Autowired
	private GuestRepository gr;
	
	@RequestMapping(value="/addEvent", method=RequestMethod.GET)
	public String eventForm() {
		return "eventForm";
	}
	@RequestMapping(value="/addEvent", method=RequestMethod.POST)
	public String eventForm(@Valid Event event, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fields");
			return "redirect:/addEvent";
		}
		er.save(event);
		attributes.addFlashAttribute("message", "Event successfully added");
		return "redirect:/addEvent";
	}
	@RequestMapping("/events")
	public ModelAndView eventsList() {
		ModelAndView mv = new ModelAndView("index");
		Iterable <Event> events = er.findAll();
		mv.addObject("events", events);
		return mv;
	}
	@RequestMapping(value="/{code}", method=RequestMethod.GET)
	public ModelAndView eventDetails(@PathVariable("code") long code){
		Event event = er.findByCode(code);
		ModelAndView mv = new ModelAndView("eventDetails");
		mv.addObject("event", event);
		Iterable<Guest> guests = gr.findByEvent(event);
		mv.addObject("guests", guests);
		return mv;
	}
	
	@RequestMapping("/deleteEvent")
	public String deleteEvent(long code) {
		Event event = er.findByCode(code);
		er.delete(event);
		return "redirect:/events";
	}
	
	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	public String eventDetailsPost(@PathVariable("code") long code,@Valid Guest guest, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fields!");
			return "redirect:/{code}";
		}
		Event event = er.findByCode(code);
		guest.setEvent(event);
		gr.save(guest);
		attributes.addFlashAttribute("message", "Guest successfully added!");
		return "redirect:/{code}";
		
	}
	
	@RequestMapping("/deleteGuest")
	public String deleteGuest(String rg){
		Guest guest = gr.findByRg(rg);
		gr.delete(guest);
		
		Event event = guest.getEvent();
		long codeLong = event.getCode();
		String code = ""+codeLong;
		return "redirect:/"+code;
	}
}






