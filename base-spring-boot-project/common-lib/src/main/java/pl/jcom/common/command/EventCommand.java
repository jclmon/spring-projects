package pl.jcom.common.command;

import pl.jcom.common.event.AbstractEvent;

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
