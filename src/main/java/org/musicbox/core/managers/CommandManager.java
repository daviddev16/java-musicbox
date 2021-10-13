package org.musicbox.core.managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.musicbox.core.command.CommandSupply;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Received;
import org.musicbox.core.command.Usage;
import org.musicbox.core.exceptions.ParameterException;
import org.musicbox.core.utils.Utils;
import org.musicbox.models.CommandFailHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private static CommandManager commandManager;
	private final List<CommandSupply> commandSupplies;

	private CommandManager() {
		commandSupplies = new ArrayList<>();
		commandManager = this;
	}

	public static void setup() {
		new CommandManager();
	}

	public void handle(Class<?> handleClass) {

		if (handleClass == null)
			throw new NullPointerException("Unabled to handle a null object.");

		Object handledObject = null;

		try {
			handledObject = handleClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println(e.getMessage());
		}

		if(handledObject == null)
			return;

		for (Method method : handleClass.getDeclaredMethods()) {
			if (isValidCommand(method, MessageReceivedEvent.class)) {
				Link link = method.getAnnotation(Link.class);
				Usage usage = method.getAnnotation(Usage.class);
				method.setAccessible(true);
				commandSupplies.add(CommandSupply.createSupply(link, usage, method, handledObject));
			}
		}
	}

	private boolean isValidCommand(Method method, Class<?> firstParameterRequiredType) {
		return (method.getAnnotation(Link.class) != null) && (method.getParameters().length > 0)
				&& (method.getParameters()[0].getType().isAssignableFrom(firstParameterRequiredType));
	}

	private Object[] processParameterValues(Received received, CommandSupply commandSupply) throws ParameterException {

		Object[] parameterValues = null;

		if(commandSupply.splitArguments()) {

			if(!matchCommandSupply(received, commandSupply)) {
				throw new ParameterException("Parameters out of bounds.", ParameterException.OUT_OF_BOUNDS);
			}

			parameterValues = new Object[commandSupply.getParameters().length];
			parameterValues[0] = received.getEvent();

			for (int i = 0; i < received.getArguments().length; i++) {
				parameterValues[i + 1] = parse(received.getArguments()[i], commandSupply.getArguments()[i].getType());
			}

			return parameterValues;
		}

		parameterValues = new Object[2];
		parameterValues[0] = received.getEvent();
		parameterValues[1] = received.getJoinedArguments();

		return parameterValues;
	}

	private void processCommandSupply(Received received, CommandSupply commandSupply) {
		try {
			commandSupply.getMethod().invoke(commandSupply.getHandledObject(),
					processParameterValues(received, commandSupply));
		} catch (IllegalAccessException ignored) {
			/* cannot happened since setAccessible(true) */
		} catch (IllegalArgumentException | InvocationTargetException e) {
			commandException(received, commandSupply, (Exception)e.getCause());
		} catch(ParameterException e) {
			switch(e.getExceptionType()) {
			case ParameterException.TYPE_MISSMATCH:
				CommandFailHandler.getFailHandler().onTypeMissmatch(received.getEvent(), received, commandSupply);
				break;
			case ParameterException.OUT_OF_BOUNDS:
				CommandFailHandler.getFailHandler().onWrongArgumentCount(received.getEvent(), received, commandSupply);
				break;
			}
		}

	}

	private void commandException(Received received, CommandSupply commandSupply, Exception exception) {
		CommandFailHandler.getFailHandler().onThrowException(received.getEvent(), received, commandSupply, exception);
		System.out.println("error while processing a command [" + commandSupply.getCommandId() + "]");
	}

	private Object parse(String argument, Class<?> parameter) {
		try {
			if (parameter == Integer.class || parameter == int.class) {
				return Integer.parseInt(argument);
			} else if (parameter == Byte.class || parameter == byte.class) {
				return Byte.parseByte(argument);
			} else if (parameter == Short.class || parameter == short.class) {
				return Short.parseShort(argument);
			} else if (parameter == Float.class || parameter == float.class) {
				return Float.parseFloat(argument);
			} else if (parameter == Double.class || parameter == double.class) {
				return Double.parseDouble(argument);
			} else if (parameter == Boolean.class || parameter == boolean.class) {
				return parseBoolean(argument);
			} else if (parameter == Long.class || parameter == long.class) {
				return Long.parseLong(argument);
			} else if (parameter == String.class) {
				return argument.trim();
			} else {
				throw new ParameterException("Type missmatch.", ParameterException.TYPE_MISSMATCH);
			}
		} catch (Exception e) {
			throw new ParameterException("Type missmatch.", ParameterException.TYPE_MISSMATCH);
		}
	}

	public void perform(String prefix, MessageReceivedEvent event) {

		if (prefix == null || event == null)
			throw new NullPointerException("'prefix' or 'event' cannot be null.");

		Received received = Received.read(prefix, event);

		if (!received.getContent().startsWith(prefix)) {
			return;
		}

		CommandSupply commandSupply = findCommandSupply(received.getName());
		if (commandSupply != null) {
			processCommandSupply(received, commandSupply);
			return;
		}
		CommandFailHandler.getFailHandler().onNotFound(event, received, commandSupply);
	}

	private CommandSupply findCommandSupply(String name) {
		return commandSupplies.stream().filter(commandSupply -> commandSupply.isMine(name)).findAny().orElse(null);
	}

	private boolean parseBoolean(String value) {
		if(Utils.isBoolean(value)) {
			return Utils.getBoolean(value);
		}
		throw new IllegalArgumentException("Not a boolean value.");
	}

	private boolean matchCommandSupply(Received received, CommandSupply commandSupply) {
		if (received.getArguments().length != commandSupply.getArguments().length) {
			return false;
		}
		return true;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public List<CommandSupply> getCommandSupplies() {
		return commandSupplies;
	}

}
