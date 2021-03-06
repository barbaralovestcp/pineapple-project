package org.pineapple;

public enum CommandSMTP implements ICommand {
    EHLO,
    MAIL,
    RCPT,
    DATA,
    RSET,
    QUIT;

    public static void printCommandNames() {
        String log = "Available SMTP Commands : ";
        for ( CommandSMTP command : CommandSMTP.values() ) {
            log += command.toString() + "  ";
        }
        System.out.println(log);
    }
}
