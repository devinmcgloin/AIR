package main;

import r.TreeNode;

import java.util.Scanner;

/**
 *
 */
public class Main{
    public static void main(String[] args) {


        Terminal terminal = new Terminal();

        Scanner console = new Scanner(System.in);
        String input;

        System.out.println("WELCOME TO AIR.\n");
        System.out.println(terminal.getHelp());


        while(true){
            //Display where we are in the folder hierarchy
            System.out.print(terminal.formatPrompt());


            //Wait for next input
            input = console.nextLine();

            //Exit terminal & Save DB
            if(input.equals("Q") || input.equals("q")){
                terminal.save();   //which calls DBInterface.save()
                break;
            }

            //Get DB response (might make it return error and then the dir?)
            TreeNode hit = terminal.parse(input);
            if(hit != null){
                System.out.println("Invalid command");

            }
        }
    }
}
