package org.musicbox.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.musicbox.annotations.Command;
import org.musicbox.annotations.DefaultDescription;
import org.musicbox.annotations.Description;
import org.musicbox.annotations.Hidden;
import org.musicbox.models.Placeholders;
import org.musicbox.utils.Utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandController {

  private Class<? extends CommandTable> tableClass;
  private CommandTable instance;
  private final Map<String[], CommandInfo> commandMap;

  public CommandController() {
	this.commandMap = new HashMap<String[], CommandInfo>();
  }

  public void register(Class<? extends CommandTable> tableClass) {
	this.tableClass = tableClass;

	try {
	  this.instance = this.tableClass.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
	  e.printStackTrace();
	}

	handleClassMethods(this.tableClass);
  }

  private void handleClassMethods(Class<?> tableClass) {

	for (Method method : tableClass.getDeclaredMethods()) {

	  Command commandAnn = method.getAnnotation(Command.class);

	  if (commandAnn != null) {
		Parameter[] methodParameters = method.getParameters();

		if (methodParameters.length == 0
			|| !methodParameters[0].getType().isAssignableFrom(MessageReceivedEvent.class)) {
		  return;
		}
		method.setAccessible(true);
		Parameter[] parameters = new Parameter[methodParameters.length - 1];
		parameters = Arrays.copyOfRange(methodParameters, 1, methodParameters.length);

		Description description = method.getAnnotation(Description.class);

		if (description == null)
		  description = DefaultDescription.get();

		commandMap.put(commandAnn.name(), new CommandInfo(parameters, tableClass, method, commandAnn, description));
	  }
	}
  }

  private void processCommand(MessageReceivedEvent event, String prefix) {

	Message message = event.getMessage();
	String content = message.getContentDisplay().trim();
	String[] separated = content.split("\\s+", 4);

	if (!separated[0].startsWith(prefix)) {
	  return;
	}

	String commandName = separated[0].substring(prefix.length());
	CommandInfo commandInfo = get(commandName);

	if (commandInfo == null) {
	  /* just ignore for a while. */
	  return;
	}

	if (!commandInfo.isAliasSplit() && separated.length > 1) {

	  Object[] parsedObjects = new Object[2];
	  parsedObjects[0] = event; /* setting the event at first index */
	  parsedObjects[1] = getAsString(separated, 1);

	  try {
		commandInfo.getCommandMethod().invoke(instance, parsedObjects);
	  } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		System.out.println("failed on processing command handlers. [1]");
		System.exit(-1);
	  }
	  return;
	}

	String[] arguments = Arrays.copyOfRange(separated, 1, separated.length);

	if (arguments.length != commandInfo.getParameters().length) {
	  Utils.send(event.getTextChannel(), "wrongCommandAlias", Placeholders.ofWrongCommandAlias(event, commandInfo.getUsage()));
	  return;
	}

	Object[] parsedObjects = new Object[arguments.length + 1];
	parsedObjects[0] = event; /* setting the event at first parameter */

	try {
	  for (int i = 0; i < arguments.length; i++) {
		parsedObjects[i + 1] = parseArgument(commandInfo.getParameters()[i].getType(), arguments[i]);
	  }
	} catch (IllegalArgumentException e) {
	  Utils.send(event.getTextChannel(), "wrongCommandAlias", Placeholders.ofWrongCommandAlias(event, commandInfo.getUsage()));
	  return;
	}

	try {
	  commandInfo.getCommandMethod().invoke(instance, parsedObjects);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	  e.printStackTrace();
	  System.out.println("failed on processing command handlers. [2]");
	  System.exit(-1);
	}

  }

  private String getAsString(String[] str, int startOffset) {
	StringBuffer buffer = new StringBuffer();
	for (int i = startOffset; i < str.length; i++) {
	  buffer.append(str[i]).append(' ');
	}
	return buffer.toString().trim();
  }

  private CommandInfo get(String command) {
	for (Entry<String[], CommandInfo> commandEntry : commandMap.entrySet()) {
	  for (String commandKey : commandEntry.getKey()) {
		if (commandKey.equals(command)) {
		  return commandEntry.getValue();
		}
	  }
	}
	return null;
  }

  private Object parseArgument(Class<?> parameterClass, String value) {

	try {
	  if (parameterClass == String.class) {
		return value;
	  } else if (parameterClass == int.class || parameterClass == Integer.class) {
		return Integer.valueOf(value);
	  } else if (parameterClass == long.class || parameterClass == Long.class) {
		return Long.valueOf(value);
	  } else if (parameterClass == boolean.class || parameterClass == Boolean.class) {
		return parseBooleanArgument(value);
	  } else if (parameterClass == float.class || parameterClass == Float.class) {
		return Float.valueOf(value);
	  } else if (parameterClass == double.class || parameterClass == Double.class) {
		return Double.valueOf(value);
	  } else {
		throw new IllegalArgumentException();
	  }
	} catch (NumberFormatException ignored) {
	  throw new IllegalArgumentException();
	}

  }

  private boolean parseBooleanArgument(String value) {
	if ("yes".equals(value) || "true".equals(value)) {
	  return true;
	} else if ("no".equals(value) || "false".equals(value)) {
	  return false;
	} else {
	  int integerValue = Integer.valueOf(value);

	  if (integerValue == 1) {
		return true;
	  } else if (integerValue == 0) {
		return false;
	  } else {
		throw new IllegalArgumentException();
	  }
	}
  }

  public void handle(MessageReceivedEvent event, String prefix) {
	processCommand(event, prefix);
  }

  public Map<String[], CommandInfo> getCommandMap() {
	return commandMap;
  }

  public final class CommandInfo {

	private final Parameter[] parameters;
	private final Class<?> table;
	private final Method commandMethod;
	private final Description description;
	private final Command commandAnnotation;

	public CommandInfo(Parameter[] parameters, Class<?> table, Method commandMethod, Command commandAnnotation,
		Description description) {
	  this.parameters = parameters;
	  this.commandMethod = commandMethod;
	  this.table = table;
	  this.commandAnnotation = commandAnnotation;
	  this.description = description;
	}

	public boolean isHidden() {
	  return commandMethod.getAnnotation(Hidden.class) != null;
	}

	public int getOrder() {
	  return commandAnnotation.order();
	}

	public String getUsage() {
	  return commandAnnotation.usage();
	}

	public boolean isAliasSplit() {
	  return commandAnnotation.aliasSplit();
	}

	public String[] getNames() {
	  return commandAnnotation.name();
	}

	public Description getDescription() {
	  return this.description;
	}

	public Method getCommandMethod() {
	  return commandMethod;
	}

	public Parameter[] getParameters() {
	  return parameters;
	}

	public Class<?> getTable() {
	  return table;
	}

  }

}
