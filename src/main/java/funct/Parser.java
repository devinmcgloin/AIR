package funct;

import memory.Notepad;
import molecule.Molecule;
import org.javatuples.Triplet;
import pa.Node;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/5/15.
 */
public class Parser {

    public static Triplet<String, String, ArrayList<String>> parseFull(String command) {
        //Have the full class and method name
        int firstPeriod = command.indexOf(".");
        int period = command.indexOf(".", firstPeriod + 1);

        String className = command.substring(0, period);
        String methodName = command.substring(period + 1, command.indexOf(" ")).trim();
        String everythingElse = command.substring(command.indexOf(" ") + 1, command.length());

        ArrayList<String> arguments = new ArrayList<>();
        for (String term : everythingElse.split(",")) {
            arguments.add(term.trim());
        }
        return new Triplet<>(className, methodName, arguments);
    }

    public static boolean isDefault(String msg) {
        String[] split = msg.split(" ");
        if (split[1].equals("has")) {
            if (split.length == 3) {
                return true;
            } else return split.length == 4;
        } else if (split[1].equals("is")) {
            return split.length == 3;
        } else return false;

    }

    public static Molecule parseDefault(String msg) {
        String[] split = msg.split(" ");
        if (split[1].equals("has")) {
            if (split.length == 3) {
                return new Molecule(getNode(split[0]), getNode(split[1]), getNode(split[2]));
            } else if (split.length == 4) {
                // todo need to reduce here
                return null;
            } else return null;
        } else if (split[1].equals("is")) {
            if (split.length == 3) {
                return new Molecule(getNode(split[0]), getNode(split[1]), getNode(split[2]));
            } else return null;
        } else return null;
    }

    private static Node getNode(String nodeName) {
        return Notepad.search(nodeName);
    }

}
