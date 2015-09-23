package funct;


import java.util.Scanner;

/**
 * Created by devinmcgloin on 8/28/15.
 */
public class Pauser {

    Scanner input = new Scanner(System.in);

    private Pauser() {
    }

    public boolean trueFalse(String s) {
        Core.println(s);
        return input.hasNextBoolean();
    }

    public int tree(String s) {
        Core.println(s);
        return input.nextInt();
    }

}
