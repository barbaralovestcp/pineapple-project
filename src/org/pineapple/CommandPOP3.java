package org.pineapple;

public enum CommandPOP3 implements ICommand{
    APOP,
    STAT,
    RETR,
    QUIT;
    /*
    USER,
    PASS,
    DELE,
    LIST,
    NIL;*/

    public static void printCommandNames() {
        String log = "Available POP3 Commands : ";
        for ( CommandPOP3 command : CommandPOP3.values() ) {
            log += command.toString() + "  ";
        }
        System.out.println(log);
    }
}
