package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <pre>
 *     // Instantiate an "OK_MAILDROP_READY" code:
 *     CodeOK ok1 = new CodeOK(CodeOK.CodeEnum.OK_MAILDROP_READY);
 *     send(ok1.toString());
 *
 *     // Instantiate an "OK_SERVER_READY" code:
 *     Code ok2 = new CodeOK(CodeOK.CodeEnum.OK_SERVER_READY);
 *     send(ok2.toString("server_name"));
 * </pre>
 */
public class CodeOK extends Code implements ParametrizedToString {
	
	public enum CodeEnum implements ICodeEnum {
		OK(parameter -> ""),
		OK_SERVER_READY(parameter -> {
			if (parameter == null)
				throw new NullPointerException("OK_SERVER_READY needs a parameter: The name of the server.");
			
			return parameter + " POP3 server ready";
		}),
		OK_MAILDROP_READY(parameter -> "maildrop locked and ready"),
		OK_VALID_MAILBOX(parameter -> {
			if (parameter == null)
				throw new NullPointerException("OK_VALID_MAILBOX needs a parameter: Name of the mailbox.");

			return parameter + " is a valid mailbox";
		}),
		OK_SCAN_LISTING(parameter -> {
			if (parameter == null)
				throw new NullPointerException("OK_SCAN_LISTING needs a parameter: The list of files");
			
			return parameter;
		}),
		OK_STAT(parameter -> {
			if (parameter == null)
				throw new NullPointerException("OK_STAT needs a parameter: The number and size of the messages");
			
			return parameter;
		}),
		OK_RETRIEVE(parameter -> {
			if (parameter == null)
				throw new NullPointerException("OK_RETRIEVE needs a parameter: The mail content");
			
			return parameter;
		}),
		OK_DELETE(parameter -> "message deleted"),;
		
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
	
	public CodeOK(ICodeEnum codeType) {
		super(codeType);
	}
	public CodeOK() {
		super(CodeEnum.OK);
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
