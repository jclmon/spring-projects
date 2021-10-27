package com.ms.core.common.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.ms.core.common.event.AbstractEvent;

@SuppressWarnings("rawtypes")
public class Commander implements ApplicationListener<AbstractEvent> {

	private final Log log = LogFactory.getLog(Commander.class);

	public static String executeAt = "executeAt";
	public static String executeWithShedule = "executeWithShedule";
	
	private final TaskExecutor taskExecutor;
	private final TaskScheduler taskScheduler;

	public Commander(TaskExecutor taskExecutor, TaskScheduler taskScheduler) {
		super();
		this.taskExecutor = taskExecutor;
		this.taskScheduler = taskScheduler;
	}

	private final Map<Class<? extends ApplicationEvent>, List<EventCommand>> commands = new HashMap<>();

	public void executeSyncCommand(AbstractCommand command) {
		log.info("Execute command: " + command);
		command.execute();
	}

	public void executeAsyncCommand(AbstractCommand command) {
		log.info("Execute command: " + command);
		this.taskExecutor.execute(command);
	}

	public void executeCommandAt(AbstractCommand command, Date date) {
		log.info("Execute command: " + command);
		this.taskScheduler.schedule(command, date);
	}
	
	private void executeCommandCron(AbstractCommand command, String shedule) {
		log.info("Execute command: " + command + " with shedule: " + shedule);
		CronTrigger cronTrigger = new CronTrigger(shedule);
		this.taskScheduler.schedule(command, cronTrigger);
		
	}

	public void executeCommandOnEvent(Class<? extends ApplicationEvent> event, EventCommand command) {
		if (commands.containsKey(event)) {
			commands.get(event).add(command);
		} else {
			List<EventCommand> commandList = new ArrayList<>();
			commandList.add(command);
			commands.put(event, commandList);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(AbstractEvent event) {
		List<EventCommand> commandsList = commands.get(event.getClass());
		if (commandsList != null) {
			for (EventCommand<?> command : commandsList) {
				command.setEvent(event);
				if (event.getSource().getMetaData().containsKey(executeAt)) {
					Date executeAtDate = (Date) event.getSource().getMetaData().get(executeAt);
					this.executeCommandAt(command, executeAtDate);
					
				} else if (event.getSource().getMetaData().containsKey(executeWithShedule)) {
					String executeShedule = (String) event.getSource().getMetaData().get(executeWithShedule);					
					this.executeCommandCron(command, executeShedule);
						
				} else {
					this.executeAsyncCommand(command);
					
				}
			}
		}

	}

}
