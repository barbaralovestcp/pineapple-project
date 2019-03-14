package org.pineapple.client;

public enum Command {
    APOP,
    STAT,
    RETR,
    QUIT;

    @Override
    public String toString() {
        return "Command{}";
    }
}
