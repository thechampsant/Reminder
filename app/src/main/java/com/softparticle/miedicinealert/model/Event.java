package com.softparticle.miedicinealert.model;

import java.util.Date;

public class Event {
	
	private long id;
	private String name;
	private String info;
	private Period period;
	private Date start_time;
	
	public Event() {
		id = -1;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setPeriod(Period period) {
		this.period = period;
	}
	
	public Period getPeriod() {
		return period;
	}
	
	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	
	public Date getStartTime() {
		return start_time;
	}

}
