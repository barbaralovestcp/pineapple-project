package org.pineapple.server.stateMachine.test;

import org.pineapple.CommandPOP3;
import org.pineapple.server.stateMachine.*;
import org.pineapple.server.stateMachine.Exception.InvalidPOP3CommandException;

import java.util.Scanner;

public class TestStateMachine {

    public static void main(String[] args) {

        String msg = "";
        Scanner in = new Scanner(System.in);

        CommandPOP3.printCommandNames();
        Context stateMachine = new Context();

        do {
            System.out.println("\nInput a command : ");
            msg = in.nextLine();
            System.out.println();

            try {

                stateMachine.handle(msg);
                /* ALTERNATIVE IMPLEMENTATION
                InputStateMachine input = new InputStateMachine(msg);
                stateMachine.handle(input.getCommand(), input.getArguments()); */
            }
            catch (InvalidPOP3CommandException err){
                System.out.println(err.getMessage());
            }
            /*

            //ALTERNATIVE VALID IMPLEMENTATION

            if (InputStateMachine.IsValidPOP3Request(msg)) {

                InputStateMachine input = new InputStateMachine(msg);
                stateMachine.handle(input.getCommand(), input.getArguments());

            }
            else {
                System.out.println("Invalid POP3 Command");
            } */

        } while (!msg.toUpperCase().equals("QUIT"));

        in.close();


    }

}
