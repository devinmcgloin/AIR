package main;

import pa.PA;
import r.TreeNode;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 6/3/15.
 * Terminal is a command parser for the time being.
 */
public class Terminal {

    pa.PA PA = new PA();


    Terminal(){

    }

    public TreeNode parse(String input){



        input = input.trim();
        String words[] = input.split(" ");
//        for(String word : words){
//            System.out.print("["+word+"]");
//        }

        //check for keywords
        if(words[0].equals("") || words[0].equals(" "))
            return null;
        else if( words[0].equals("ADD") && words.length>1  ){
            input = input.replaceAll("ADD", "");
            input = input.trim();
            input = input.toLowerCase();
            PA.addChild(input);
        }
        else if(input.startsWith("+")){
            input = input.replace('+', ' ');
            input = input.trim();
            input = input.toLowerCase();
            PA.addChild(input);
        }
        else if(words[0].equals("DEL") && words.length>1){
            input = input.replaceAll("DEL", "");
            input = input.trim();
            PA.del(input);
        }
        else if(input.startsWith("-")){
            input = input.replace('-', ' ');
            input = input.trim();
            PA.del(input);
        }
        else if(words[0].equals("PRINT") || words[0].equals("ls")){
            System.out.println(printChildren());
        }
        else if(words[0].equals("BACK") || words[0].equals("..")){
            PA.toParent();
        }
        else if(words[0].equals("CD") || words[0].equals("cd")){
            PA.toRoot();
        }
        else if(words[0].equals("RENAME") || words[0].equals("mv")){
            input = input.replaceAll("mv", "");
            input = input.replaceAll("RENAME", "");
            input = input.trim();
            PA.rename(words[1], words[2]);
        }
        //Fun stuff
        else{
            if(PA.contains(input)) {
                PA.toChild(input);
            }
            else {
                PA.addChild(input);
            }
        }

    return null;


    }

    //Calls save for whole project
    public void save(){

        PA.save();

    }

    public String printChildren(){
        ArrayList<String> children = PA.getChildren();
        String returnString = "";
        for (String child : children)
            returnString += child + "    ";
        return returnString;
    }

    public String formatPrompt(){
        String path = "";
        for (String term : PA.getPath()){
            path += term + "/";
        }
        return path;
    }

    public static void main(String[] args) {
        System.out.println("SYSTEM LIVE");

        Terminal terminal = new Terminal();

        Scanner console = new Scanner(System.in);
        String input;


        while(true){
            //Display where we are in the folder hierarchy
            System.out.print(terminal.formatPrompt());


            //Wait for next input
            input = console.nextLine();

            //Exit terminal & Save DB
            if(input.equals("q")){
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
