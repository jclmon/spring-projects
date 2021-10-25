package com.ms.core.common.event;

import java.io.Serializable;

public class RemoteEvent<T> extends AbstractEvent<T>implements Serializable, SendRemote{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4088715604882690142L;

	public RemoteEvent(EventData<T> source) {
		super(source);
	}

}
