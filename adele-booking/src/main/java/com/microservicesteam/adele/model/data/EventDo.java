package com.microservicesteam.adele.model.data;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.microservicesteam.adele.model.Event;
import com.microservicesteam.adele.model.EventStatus;
import com.microservicesteam.adele.model.Venue;

@Entity
public class EventDo implements Event {
	
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	private String description;
	
	private EventStatus status;
	
	private LocalDateTime dateTime;
	
	public EventDo() {
	}

	public EventDo(String name, String description, EventStatus status, LocalDateTime dateTime) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
		this.dateTime = dateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Venue venue() {
		return null;
	}

	@Override
	public LocalDateTime dateTime() {
		return dateTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public EventStatus status() {
		return status;
	}

	@Override
	public String toString() {
		return "EventDo [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status
				+ ", dateTime=" + dateTime + "]";
	}

}
