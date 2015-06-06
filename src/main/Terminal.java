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



        input = input.trim().toLowerCase();
        String words[] = input.split(" ");
//        for(String word : words){
//            System.out.print("["+word+"]");
//        }

        //check for keywords
        if(words[0].equals("") || words[0].equals(" "))
            return null;
        else if(words[0].equals("help") || words[0].equals("?")){
            System.out.print(getHelp());
        }
        else if( words[0].equals("add") && words.length>1  ){
            input = input.replaceAll("add", "");
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
        else if(words[0].equals("del") && words.length>1){
            input = input.replaceAll("del", "");
            input = input.trim();
            PA.del(input);
        }
        else if(input.startsWith("-")){
            input = input.replace('-', ' ');
            input = input.trim();
            PA.del(input);
        }
        else if(words[0].equals("print") || words[0].equals("ls")){
            System.out.println(printChildren());
        }
        else if(words[0].equals("back") || words[0].equals("..")){
            PA.toParent();
        }
        else if(words[0].equals("cd") || words[0].equals("cd")){
            PA.toRoot();
        }
        else if(words[0].equals("rename") || words[0].equals("mv")){
            input = input.replaceAll("mv", "");
            input = input.replaceAll("rename", "");
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

    public String getHelp(){
        return String.format(
                        "PATHS must be formatted as follows:\n'nouns/places/nations'\n" +
                        "(ADD    | +  ): Add node at current directory\n" +
                        "(DEL    | -  ): PATH\n" +
                        "(PRINT  | ls ): List directory.\n" +
                        "(BACK   | .. ): Back one level.\n" +
                        "(CD     | cd ): Return to root.\n" +
                        "(RENAME | mv ): PATH - newName\n" +
                        "(Help   | ?  ): For help.\n\n");
    }

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
