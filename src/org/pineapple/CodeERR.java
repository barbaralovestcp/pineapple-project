package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeERR extends Code implements ParametrizedToString {
	
	public enum CodeEnum implements ICodeEnum {
		ERR_PERMISSION_DENIED(parameter -> "Permission denied"),
		ERR_NO_MAILBOX(parameter -> {
			if (parameter == null)
				throw new NullPointerException("ERR_NO_MAILBOX needs a parameter: The name of the server.");
			
			return "sorry no mailbox for " + parameter;
		}),
		ERR_INVALID_PASSWORD(parameter -> "invalid password"),
		ERR_DEL_MESSAGE_NOT_REMOVED(parameter -> "some deleted message not removed");
		
		@NotNull
		private ICodeEnum toString;
		
		CodeEnum(ICodeEnum toString) {
			setToString(toString);
		}
		
		@NotNull
		@Contract(pure = true)
		private ICodeEnum getToString() {
			return toString;
		}
		
		private void setToString(@NotNull ICodeEnum toString) {
			this.toString = toString;
		}
		
		@NotNull
		@Override
		public String toString(@Nullable String parameter) {
			return toString.toString(parameter);
		}
		@NotNull
		@Override
		public String toString() {
			return toString.toString(null);
		}
	}
	
	public CodeERR(ICodeEnum codeType) {
		super(codeType);
	}
	
	public void raise() throws CodeErrorException {
		throw new CodeErrorException(this);
	}
	public void raise(Throwable cause) throws CodeErrorException {
		throw new CodeErrorException(this, cause);
	}
	
	/* OVERRIDES */
	
	@NotNull
	@Override
	public String toString(@Nullable String parameter) {
		String message = getCodeType().toString(parameter);
		return "+OK" + (message.length() > 0 ? " " + message : "");
	}
	
	@NotNull
	@Override
	public String toString() {
		return toString(null);
	}
}
