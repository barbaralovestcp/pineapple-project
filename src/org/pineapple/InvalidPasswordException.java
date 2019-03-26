package org.pineapple;

public class InvalidPasswordException extends RuntimeException {
	
	public InvalidPasswordException(String message) {
		super(message);
	}
	public InvalidPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
	public InvalidPasswordException() {
		super("Invalid user/password.");
	}
}
