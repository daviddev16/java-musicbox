package org.musicbox.core.command;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.musicbox.config.DefaultConfig;

public final class CommandSupply {

	private final Link link;
	private final Object handledObject;
	private final Method method;
	private final Usage usage;

	private CommandSupply(Link link, Method method, Usage usage, Object handledObject) {
		this.link = link;
		this.method = method;
		this.handledObject = handledObject;
		this.usage = usage;
	}

	public boolean isMine(String command) {
		for (String name : link.names()) {
			if (name.equals(command)) {
				return true;
			}
		}
		return false;
	}

	public String getUsage() {
		return usage == null ? "Não informado" : DefaultConfig.PREFIX + usage.usage();
	}

	public Parameter[] getArguments() {
		return Arrays.copyOfRange(getParameters(), 1, getParameters().length);
	}

	public int getCommandId() {
		return link.commandId();
	}

	public Parameter[] getParameters() {
		return method.getParameters();
	}

	public boolean splitArguments() {
		return link.argumentsSplit();
	}

	public String[] getNames() {
		return link.names();
	}

	public Method getMethod() {
		return method;
	}

	public Object getHandledObject() {
		return handledObject;
	}

	public static CommandSupply createSupply(Link link, Usage usage, Method method, Object handledObject) {
		return new CommandSupply(link, method, usage, handledObject);
	}

}
