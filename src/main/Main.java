package main;


import java.util.ArrayList;
import java.util.Scanner;

import r.R;
import r.TreeNode;

/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 */
public class Main {

    public static void main(String[] args) {

        Nulp smart = new Nulp();

        Scanner console = new Scanner(System.in);
        String dir = "R/";
        String input;


        System.out.println("once you traverse into a db, use 'ddev' to trigger your devintest() function in PA");

        while(true){
            //Display where we are in the folder hierarchy
            System.out.print(dir);


            //Wait for next input
            input = console.nextLine();

            //Exit terminal & Save DB
            if(input.equals("q")){
                smart.save();   //which calls DBInterface.save()
                break;
            }

            //Get DB response (might make it return error and then the dir?)
           TreeNode hit = smart.parse(input);
            if(hit != null){

                //dir = hits.get(0).getAddress();
                dir = hit.getAddress();
            }



        }
    }
}
