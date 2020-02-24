package pl.jcom.common.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import pl.jcom.common.constants.ConfigConstants;

public abstract class AbstractEvent<T> extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8329882167757672345L;

	protected UUID applicationId = ConfigConstants.APPLICATION_ID;
	protected EventData<T> source;

	public AbstractEvent(EventData<T> source) {
		super(source);
		this.source = source;
	}

	public UUID getApplicationId() {
		return applicationId;
	}

	public EventData<T> getSource() {
		return source;
	}

}
