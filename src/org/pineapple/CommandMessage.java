package org.pineapple;

import org.jetbrains.annotations.NotNull;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CommandMessage {

    private static String OK = "+OK";
    private static String ERR = "-ERR";

    private CodeOK.CodeEnum codeOK;
    private CodeERR.CodeEnum codeERR;
    private ArrayList<String> parameters;

    public void parseMessage(@NotNull String message) throws BadAttributeValueExpException {
        if (message.contains(OK)) {
            System.out.println("+OK message received");
            parseOkMessage(message);
        } else if (message.contains(ERR)) {
            System.out.println("-ERR message received");
            parseErrMessage(message);
        } else {
            throw new BadAttributeValueExpException(message);
        }
        System.out.println(this.toString());
    }

    private void parseOkMessage(@NotNull String message) {
        for (CodeOK.CodeEnum code : CodeOK.CodeEnum.values()) {
            if (message.contains(code.toString(""))) {
                message = message.replace(code.toString(""), "");
                message = message.replace(OK, "");
                this.codeOK = code;
                String[] parts = message.split(" ");
                this.parameters.addAll(Arrays.asList(parts));
                break;
            }
        }
    }

    private void parseErrMessage(@NotNull String message) {
        for (CodeERR.CodeEnum code : CodeERR.CodeEnum.values()) {
            if (message.contains(code.toString(""))) {
                message = message.replace(code.toString(""), "");
                message = message.replace(ERR, "");
                this.codeERR = code;
                String[] parts = message.split(" ");
                this.parameters.addAll(Arrays.asList(parts));
                break;
            }
        }
    }


    public boolean isOKMessage() {
        return this.codeOK != null;
    }

    public boolean isERRMessage() {
        return this.codeERR != null;
    }

    //Getters and setters

    public CodeOK.CodeEnum getCodeOK() {
        return codeOK;
    }

    public void setCodeOK(CodeOK.CodeEnum codeOK) {
        this.codeOK = codeOK;
    }

    public CodeERR.CodeEnum getCodeERR() {
        return codeERR;
    }

    public void setCodeERR(CodeERR.CodeEnum codeERR) {
        this.codeERR = codeERR;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandMessage that = (CommandMessage) o;
        return getCodeOK() == that.getCodeOK() &&
                getCodeERR() == that.getCodeERR() &&
                Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodeOK(), getCodeERR(), getParameters());
    }

    @Override
    public String toString() {
        return "CommandMessage{" +
                "codeOK=" + codeOK +
                ", codeERR=" + codeERR +
                ", parameters=" + parameters +
                '}';
    }
}
