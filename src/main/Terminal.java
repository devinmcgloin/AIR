package main;

import pa.PA;
import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/3/15.
 * Terminal is a command parser for the time being.
 */
public class Terminal {

    pa.PA PA = new PA("Terminal");
    TreeNode current;


    Terminal() {
        current = PA.getTreeNode("R/");
    }

    public void parse(String input) {


        input = input.trim();
        String words[] = input.split(" ");


        //check for keywords
        if (words[0].equals("") || words[0].equals(" "))
            return;
        else if (words[0].equals("help") || words[0].equals("?")) {
            System.out.print(getHelp());
        } else if (words[0].equals("add") && words.length > 1) {
            input = input.replaceAll("add", "");
            input = input.trim();
            input = input.toLowerCase();
            PA.add(input, current.getAddress());
        } else if (input.startsWith("+")) {
            input = input.replace('+', ' ');
            input = input.trim();
            input = input.toLowerCase();
            PA.add(input, current.getAddress());
        } else if (words[0].equals("del") && words.length > 1) {
            input = input.replaceAll("del", "");
            input = input.trim();
            PA.del(input, current.getAddress());
        } else if (input.startsWith("-")) {
            input = input.replace('-', ' ');
            input = input.trim();
            PA.del(input, current.getAddress());
        } else if (words[0].equals("print") || words[0].equals("ls")) {
            System.out.println(printChildren());
        } else if (words[0].equals("back") || words[0].equals("..")) {
            current = current.getParent();
        } else if (words[0].equals("rename") || words[0].equals("mv")) {
            //TODO: Needs fixing, r.RENAME throws nullpointer when Terminal triggers rename. Due to parsing error, when nodes include spaces. - Temp solution, no spaces allowed.
            input = input.replaceAll("mv", "");
            input = input.replaceAll("rename", "");
            input = input.trim();
            PA.rename(words[1], words[2]);
        } else if (words[0].equals("bdev")) {
            //System.out.println("PA's Current:       " + current.getName() + " -  " + current.getAddress());
            PA.blazetest();
        }else if (words[0].equals("ddev")) {
            System.out.println("PA's Current:       " + current.getName() + " -  " + current.getAddress());
            PA.devintest();
        }
        else if (words[0].equals((";PQ"))){
            PA.query();
        }
        //Fun stuff
        else {

            if (current.getAddress().equals("R/")) {
                current = PA.getTreeNode("R/" + input);
            } else {
                if (current.contains(input)) {
                    current = PA.getTreeNode(current.getAddress() + input);
                }

            }

        }
    }

    public String printChildren() {
        ArrayList<String> children = current.getChildrenString();
        String returnString = "";
        for (String child : children)
            returnString += child + "    ";
        return returnString;
    }

    public String getHelp() {
        return String.format(
                "PATHS must be formatted as follows:\n" +
                        "'nouns/places/nations'\n" +
                        "\nCommands:\n" +
                        "(ADD    | +  ): Add node at current directory\n" +
                        "(DEL    | -  ): PATH\n" +
                        "(PRINT  | ls ): List directory.\n" +
                        "(BACK   | .. ): Back one level.\n" +
                        "(CD     | cd ): Return to root.\n" +
                        "(RENAME | mv ): PATH - newName\n" +
                        "(;PQ         ): Activates the PA Query System\n" +
                        "(Help   | ?  ): For help.\n\n");
    }

    //Calls save for whole project
    public void save() {

        PA.save();

    }

}
