package org.pineapple;

public class MalformedMessageException extends Exception {
	
	public MalformedMessageException(String message) {
		super(message);
	}
	public MalformedMessageException(String message, Throwable cause) {
		super(message, cause);
	}
	public MalformedMessageException(Throwable cause) {
		super(cause);
	}
	public MalformedMessageException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
		super(message, cause, enableSuppression, writeableStackTrace);
	}
}
