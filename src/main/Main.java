package main;


import java.util.Scanner;

import pa.PA;
import r.TreeNode;

/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("SYSTEM LIVE");

        Terminal terminal = new Terminal();
        PA PA = new PA();

        Scanner console = new Scanner(System.in);
        String dir = "R/";
        String input;


        while(true){
            //Display where we are in the folder hierarchy
            System.out.print(dir);


            //Wait for next input
            input = console.nextLine();

            //Exit terminal & Save DB
            if(input.equals("q")){
                terminal.save();   //which calls DBInterface.save()
                break;
            }

            if(input.equals("t")){
                System.out.println("TEST TRIGGERED: ");
                PA.inherit("tmp.txt/CEO", "tmp.txt/Accountant", "has");
                terminal.save();
                break;
            }

            //Get DB response (might make it return error and then the dir?)
           TreeNode hit = terminal.parse(input);
            if(hit != null){

                //dir = hits.get(0).getAddress();
                dir = hit.getAddress();
            }
        }
    }
}
