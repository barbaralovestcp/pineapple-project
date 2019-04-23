package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeOKSMTP extends Code implements ParametrizedToString {

    public enum CodeEnum implements ICodeEnum {
        OK(parameter -> "250 OK"),
        OK_CONNECTED(parameter -> {
            if (parameter == null)
                throw new NullPointerException("OK CONNECTED needs a parameter: Name of the server.");

            return  "220 " + parameter + " Simple Mail Transfer Service Ready";
        }),
        OK_GREETING(parameter -> {
            if (parameter == null)
                throw new NullPointerException("OK GREETING needs a parameter: Name of the client.");

            return  "250 greets " + parameter;
        }),
        OK_MAIL_FROM(parameter -> "250 mail from received"),
        OK_USER_FOUND(parameter -> "250 user found"),
        OK_DATA_RECEIVED(parameter -> "250 data received"),
        OK_QUIT(parameter -> "250 quit"),
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
        return (message.length() > 0 ? message : "") + "\r\n";
    }

    @NotNull
    @Override
    public String toString() {
        return toString(null);
    }
}
