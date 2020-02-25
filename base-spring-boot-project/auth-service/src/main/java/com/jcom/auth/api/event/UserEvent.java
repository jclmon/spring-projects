package com.jcom.auth.api.event;

import java.util.Date;

import com.jcom.auth.api.model.User;

import pl.jcom.common.command.Commander;
import pl.jcom.common.event.EventData;
import pl.jcom.common.event.RemoteEvent;

public class UserEvent extends RemoteEvent<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6903340656147067369L;

	/**
	 * Ejecución sincrona
	 * 
	 * @param user
	 */
	public UserEvent(User user) {
		super(new EventData<User>());		
		this.source.setData(user);		
	}

	/**
	 * Ejecución asincrona, asigna fecha de ejecución
	 * 
	 * @param user
	 * @param fecha
	 */
	public UserEvent(User user, Date fecha) {
		super(new EventData<User>());		
		getSource().getMetaData().put(Commander.executeAt, fecha);
		this.source.setData(user);		
	}

	/**
	 * Ejecución asincrona, con cron
	 * ver:
	 * https://www.freeformatter.com/cron-expression-generator-quartz.html
	 * 
	 * @param user
	 * @param shedule pe "0 0 0 ? * * *"
	 */
	public UserEvent(User user, String shedule) {
		super(new EventData<User>());		
		getSource().getMetaData().put(Commander.executeWithShedule, shedule);
		this.source.setData(user);
	}

}
