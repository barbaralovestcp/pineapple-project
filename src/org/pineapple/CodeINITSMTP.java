package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeINITSMTP extends Code implements ParametrizedToString{
    public enum CodeEnum implements ICodeEnum {
        INIT(parameter -> ""),
        INIT_SERVICE_READY(parameter -> "Simple Mail Transfer Service Ready"),
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

    public CodeINITSMTP(ICodeEnum codeType) {
        super(codeType);
    }
    public CodeINITSMTP() {
        super(CodeINITSMTP.CodeEnum.INIT);
    }

    /* OVERRIDES */

    @NotNull
    @Override
    public String toString(@Nullable String parameter) {
        String message = getCodeType().toString(parameter);
        return "220 " + (message.length() > 0 ? " " + message : "") + "\r\n";
    }

    @NotNull
    @Override
    public String toString() {
        return toString(null);
    }
}
