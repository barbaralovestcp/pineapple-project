package org.pineapple.server.stateMachine.test;

import org.pineapple.server.stateMachine.Command;
import org.pineapple.server.stateMachine.Context;

import java.sql.SQLOutput;
import java.util.Scanner;

public class TestStateMachine {

    public static void main(String[] args) {

        Context stateMachine = new Context(new StateA());

        String msg = "";
        //Command comm;
        //String[] request;
        Scanner in = new Scanner(System.in);

        do {
            System.out.println("Input a request : ");
            msg = in.nextLine();

            //request = msg.split("\\s+");
            //comm = Command.valueOf(request[0]);

            //ajouter type retour?
            if (!msg.equals("quit")) {
                stateMachine.handle(Command.APOP_VALID, msg);
            }

        } while (!msg.equals("quit"));

        in.close();
        ///....

        //String[] request = msg.split("\\s+");
        //Command comm = Command.valueOf(request[0]);


    }

}
