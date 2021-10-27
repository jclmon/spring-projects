package com.ms.core.common.command;

public abstract class AbstractCommand implements Runnable {

	@Override
	public void run() {
		execute();
	}

	protected abstract void execute();

}
