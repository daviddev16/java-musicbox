package org.musicbox.core.exceptions;

public class ParameterException extends RuntimeException {

	private static final long serialVersionUID = -2277469739389226310L;

	public static final int TYPE_MISSMATCH = 1;
	public static final int OUT_OF_BOUNDS = 0; 

	private int exceptionType;

	public ParameterException(String name, int exceptionType) {
		super(name);
		this.exceptionType = exceptionType;
	}

	public int getExceptionType() {
		return exceptionType;
	}

}
