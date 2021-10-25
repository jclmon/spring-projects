package com.ms.core.common.command;

import com.ms.core.common.event.AbstractEvent;

public abstract class EventCommand<T> extends AbstractCommand {

	public EventCommand() {
		super();
	}

	protected AbstractEvent<T> event;

	public AbstractEvent<T> getEvent() {
		return event;
	}

	public void setEvent(AbstractEvent<T> event) {
		this.event = event;
	}

}
