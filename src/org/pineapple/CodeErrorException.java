package org.pineapple;

import org.jetbrains.annotations.NotNull;

public class CodeErrorException extends Exception {
	
	public CodeErrorException(@NotNull CodeERR code) {
		super(code.toString());
	}
	
	public CodeErrorException(@NotNull CodeERR code, Throwable cause) {
		super(code.toString(), cause);
	}
	
	public CodeErrorException(Throwable cause) {
		super(cause);
	}
	
	public CodeErrorException(@NotNull CodeERR code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(code.toString(), cause, enableSuppression, writableStackTrace);
	}
}
