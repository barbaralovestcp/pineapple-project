package org.pineapple;

public enum CommandSMTP {
    EHLO,
    MAIL,
    RCPT,
    DATA,
    REST;

    public static void printCommandNames() {
        String log = "Available SMTP Commands : ";
        for ( CommandSMTP command : CommandSMTP.values() ) {
            log += command.toString() + "  ";
        }
        System.out.println(log);
    }
}
