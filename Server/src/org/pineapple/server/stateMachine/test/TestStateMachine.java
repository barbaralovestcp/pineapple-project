package org.pineapple.server.stateMachine.test;

import org.pineapple.server.stateMachine.*;
import java.util.Scanner;

public class TestStateMachine {

    public static void main(String[] args) {

        Context stateMachine = new Context(new StateServerListening());

        String msg = "";
        Scanner in = new Scanner(System.in);


        do {
            System.out.println("Input a command : ");
            msg = in.nextLine();

            if (InputStateMachine.IsValidPOP3Request(msg)) {

                InputStateMachine input = new InputStateMachine(msg);
                stateMachine.handle(input.getCommand(), input.getArguments());

            }
            else {
                System.out.println("Invalid POP3 Command\n");
            }

        } while (!msg.toUpperCase().equals("QUIT"));

        in.close();


    }

}
