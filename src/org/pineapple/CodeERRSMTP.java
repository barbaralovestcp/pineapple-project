package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeERRSMTP extends Code implements ParametrizedToString{

    public enum CodeEnum implements ICodeEnum {
        ERR(parameter -> ""),
        ERR_INVALID_RCPT(parameter -> "No sush user here"),
        ERR_MAIL_FROM(parameter -> "Wrong mail from");

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

    public CodeERRSMTP(ICodeEnum codeType) {
        super(codeType);
    }
    public CodeERRSMTP() {
        super(CodeERRSMTP.CodeEnum.ERR);
    }

    /* OVERRIDES */

    @NotNull
    @Override
    public String toString(@Nullable String parameter) {
        String message = getCodeType().toString(parameter);
        return "550 " + (message.length() > 0 ? " " + message : "") + "\r\n";
    }

    @NotNull
    @Override
    public String toString() {
        return toString(null);
    }
}