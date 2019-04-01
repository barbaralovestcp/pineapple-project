package org.pineapple.stateMachine.test;

import org.pineapple.CommandPOP3;
import org.pineapple.server.StateServerListening;
import org.pineapple.stateMachine.*;
import org.pineapple.stateMachine.Exception.InvalidCommandException;

import java.util.Scanner;

public class TestStateMachine {

    public static void main(String[] args) {

        String msg = "";
        Scanner in = new Scanner(System.in);

        CommandPOP3.printCommandNames();
        Context stateMachine = new Context(new StateServerListening());

        do {
            System.out.println("\nInput a command : ");
            msg = in.nextLine();
            System.out.println();

            try {

                //stateMachine.handle(msg);
                /* ALTERNATIVE IMPLEMENTATION
                InputStateMachinePOP3 input = new InputStateMachinePOP3(msg);
                stateMachine.handle(input.getCommand(), input.getArguments()); */
            }
            catch (InvalidCommandException err){
                System.out.println(err.getMessage());
            }
            /*

            //ALTERNATIVE VALID IMPLEMENTATION

            if (InputStateMachinePOP3.IsValidPOP3Request(msg)) {

                InputStateMachinePOP3 input = new InputStateMachinePOP3(msg);
                stateMachine.handle(input.getCommand(), input.getArguments());

            }
            else {
                System.out.println("Invalid POP3 Command");
            } */

        } while (!msg.toUpperCase().equals("QUIT"));

        in.close();


    }

}
