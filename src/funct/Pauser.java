package funct;


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

}
