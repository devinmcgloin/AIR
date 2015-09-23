package funct;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 8/28/15.
 */
public class Pauser {


    private Pauser() {
    }

    public static boolean trueFalse(String s) {
        Scanner input = new Scanner(System.in);
        Core.println(s + " (true | false)");
        return input.hasNextBoolean();
    }

    public static int tree(String s) {
        Scanner input = new Scanner(System.in);
        Core.println(s);
        return input.nextInt();
    }

    public static <T> int whichOne(ArrayList<T> list) {
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < list.size(); i++) {
            Core.print(String.format("(%2d) = %s", i, list.get(i).toString()));
        }
        return input.nextInt();
    }

}
