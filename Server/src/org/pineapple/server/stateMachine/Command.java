package org.pineapple.server.stateMachine;

public enum Command {
    APOP_VALID,
    APOP_INVALID,
    USER,
    PASS_VALID,
    PASS_INVALID,
    QUIT_VALID,
    QUIT_INVALID,
    DELE,
    RETR,
    STAT,
    LIST
}
