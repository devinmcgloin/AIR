package funct;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author devinmcgloin
 * @version 8/28/15
 */
public class Pauser {


    private Pauser() {
    }

    public static boolean trueFalse(String s) {
        Scanner input = new Scanner(System.in);
        Core.println(s + " (true | false)");
        Core.print(">>> ");
        String line = input.next();
        return line.matches("(true|t|yes|T|True)");
    }

    public static int tree(String s) {
        Scanner input = new Scanner(System.in);
        Core.println(s);
        Core.print(">>> ");
        String line = input.next();
        if (line.matches("\\d+")) {
            return Integer.parseInt(line);
        } else {
            return -1;
        }
    }

    public static <T> int whichOne(ArrayList<T> list) {
        Scanner input = new Scanner(System.in);
        Core.println("Enter a positive number to select a node, negative number escapes.");
        for (int i = 0; i < list.size(); i++) {
            Core.print(String.format("( %2d ) = %s\n", i, list.get(i).toString()));
        }
        Core.print(">>> ");
        String line = input.next();
        return line.matches("d+") ? Integer.parseInt(line) : -1;
    }

}
