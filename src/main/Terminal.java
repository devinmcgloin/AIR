package main;

import pa.PA;
import r.TreeNode;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 6/3/15.
 * Terminal is a command parser for the time being.
 *
 */
public class Terminal {

    pa.PA PA = new PA();
    TreeNode current;


    Terminal(){
        current = PA.get("R/");
    }

    public TreeNode parse(String input){



        input = input.trim();
        String words[] = input.split(" ");


        //check for keywords
        if( words[0].equals("ADD") && words.length>1  ){
            input = input.replaceAll("ADD", "");
            input = input.trim();

            PA.add(input, current.getAddress());


        }
        else if(input.startsWith("+")){
            input = input.replace('+', ' ');
            input = input.trim();
            PA.add(input, current.getAddress());
        }
        else if(words[0].equals("DEL") && words.length>1){
            input = input.replaceAll("DEL", "");
            input = input.trim();
            PA.del(input, current.getAddress());
        }
        else if(input.startsWith("-")){
            input = input.replace('-', ' ');
            input = input.trim();
            PA.del(input, current.getAddress());
        }
        else if(words[0].equals("PRINT") || words[0].equals("ls")){

            //current.printChildren();

            for(int i=0; i<current.children.size(); i++){
                System.out.println(current.children.get(i).getName());
            }
        }
        else if(words[0].equals("BACK") || words[0].equals("..")){
            current =  current.getParent();

        }
        else if(words[0].equals("RENAME") || words[0].equals("mv")){
            input = input.replaceAll("mv", "");
            input = input.replaceAll("RENAME", "");
            input = input.trim();
            PA.rename(input, current.getAddress());
            //TODO: genTree.current.updateAddress();
        }
        else if( words[0].equals("PARENT") ){
            input = input.replaceAll("PARENT", "");
            input = input.trim();
            PA.addParent(input, current.getAddress());
        }
        else if(words[0].equals("ddev")){
            PA.devintest();
        }
        else if(words[0].equals("bdev")){
            PA.blazetest();
        }
        //Fun stuff
        else{

            if(current.getName().equals("R/")){
                current = PA.get("R/" + input);
            }else {
                current = PA.get(current.getAddress() + input);
            }

        }



        return current;
    }

    //Calls save for whole project
    public void save(){

        PA.save();

    }

    public String printChildren(){
        ArrayList<String> children = PA.getChildren(getAddress());
        String returnString = "";
        for (String child : children)
            returnString += child + "    ";
        return returnString;
    }

    public String getHelp(){
        return String.format(
                        "PATHS must be formatted as follows:\n" +
                                "'nouns/places/nations'\n" +
                                "\nCommands:\n"+
                        "(ADD    | +  ): Add node at current directory\n" +
                        "(DEL    | -  ): PATH\n" +
                        "(PRINT  | ls ): List directory.\n" +
                        "(BACK   | .. ): Back one level.\n" +
                        "(CD     | cd ): Return to root.\n" +
                        "(RENAME | mv ): PATH - newName\n" +
                        "(Help   | ?  ): For help.\n\n");
    }

    public String getAddress(){
        return PA.getAddress();
    }

    public static void main(String[] args) {


        Terminal terminal = new Terminal();

        Scanner console = new Scanner(System.in);
        String input;

        System.out.println("WELCOME TO AIR.\n");
        System.out.println(terminal.getHelp());


        while(true){
            //Display where we are in the folder hierarchy
            System.out.print(terminal.getAddress());


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
