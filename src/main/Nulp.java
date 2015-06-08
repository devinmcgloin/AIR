package main;

import pa.PA;
import r.TreeNode;

/**
 * Created by devinmcgloin on 6/3/15.
 * Nulp is a command parser for the time being. 
 */
public class Nulp {

    TreeNode current;
    pa.PA PA = new PA();


    Nulp(){

        current = PA.get("R/");
        //current.setAddress("R/");

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

}
