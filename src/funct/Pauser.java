package funct;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 8/28/15.
 */
public class Pauser {

    private static Scanner input = new Scanner(System.in);

    private Pauser() {
    }

    public static boolean trueFalse(String s) {
        Core.println(s);
        return input.hasNextBoolean();
    }

    public static int tree(String s) {
        Core.println(s);
        return input.nextInt();
    }

    public static <T> int whichOne(ArrayList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            Core.print(String.format("(%2d) = %s", i, list.get(i).toString()));
        }
        return input.nextInt();
    }

}
