package org.pineapple;

public class CodeERR extends Code {
	
	public enum CodeEnum implements ICodeEnum {
		ERR
	}
	
	public CodeERR(String message) {
		super(false, message);
	}
	
	public void raise() throws CodeErrorException {
		throw new CodeErrorException(this);
	}
	public void raise(Throwable cause) throws CodeErrorException {
		throw new CodeErrorException(this, cause);
	}
	
	/* OVERRIDES */
	
	public String toString() {
		return "-ERR " + getMessage();
	}
}
