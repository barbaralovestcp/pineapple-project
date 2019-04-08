package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeOKSMTP extends Code implements ParametrizedToString {

    public enum CodeEnum implements ICodeEnum {
        OK(parameter -> ""),
        OK_CONNECTED(parameter -> {
            if (parameter == null)
                throw new NullPointerException("OK CONNECTED needs a parameter: Name of the server.");

            return  "220 " + parameter + " Simple Mail Transfer Service Ready";
        }),
        OK_GREETING(parameter -> {
            if (parameter == null)
                throw new NullPointerException("OK GREETING needs a parameter: Name of the client.");

            return  "greets " + parameter;
        }),
        OK_MAIL_FROM(parameter -> "mail from received"),
        OK_USER_FOUND(parameter -> "user found"),
        OK_DATA_RECEIVED(parameter -> "data received"),
        OK_QUIT(parameter -> "250 OK"),
        ;

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

    public CodeOKSMTP(ICodeEnum codeType) {
        super(codeType);
    }
    public CodeOKSMTP() {
        super(CodeEnum.OK);
    }

    /* OVERRIDES */

    @NotNull
    @Override
    public String toString(@Nullable String parameter) {
        String message = getCodeType().toString(parameter);
        return "250 " + (message.length() > 0 ? " " + message : "") + "\r\n";
    }

    @NotNull
    @Override
    public String toString() {
        return toString(null);
    }
}
