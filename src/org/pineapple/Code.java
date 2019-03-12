package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for code used in POP3 protocol.
 */
public abstract class Code {
	
	/**
	 * The enum of the code. See `CodeOK.CodeEnum` and `CodeERR.CodeEnum`
	 */
	@NotNull
	private ICodeEnum codeType;
	
	/**
	 * Constructor for a code.
	 * @param codeType The enum of the code. See `CodeOK.CodeEnum` and `CodeERR.CodeEnum`
	 */
	public Code(@NotNull ICodeEnum codeType) {
		setCodeType(codeType);
	}
	
	/* GETTERS & SETTERS */
	
	@NotNull
	@Contract(pure = true)
	public ICodeEnum getCodeType() {
		return codeType;
	}
	
	public void setCodeType(@NotNull ICodeEnum codeType) {
		this.codeType = codeType;
	}
	
	/* OVERRIDES */
	
	/**
	 * The code as a string. It is the polarity (written as the sign '+' or '-') followed by the code name (OK, ERR),
	 * and the message.
	 * @return The string representation of the class.
	 */
	@Override
	public abstract String toString();
}
