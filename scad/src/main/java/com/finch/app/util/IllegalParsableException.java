package com.finch.app;

public class IllegalParsableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalParsableException() {
		super();
	}

	public IllegalParsableException(final String msg) {
		super(msg);
	}
}