package funct;

import logic.SetLogic;
import memory.Notepad;
import pa.Node;

/**
 * @author devinmcgloin
 * @version 9/29/15
 */
public class Predicate {

    public static boolean isExpression(Node expression) {
        return SetLogic.xISyP(expression, Notepad.searchByTitle("expression"));
    }

    public static boolean isLDATA(Node expression) {
        return SetLogic.xISyP(expression, Notepad.searchByTitle("ldata"));
    }

    /**
     * Takes a type of ldata, but also a string of ldata values.
     *
     * @param type
     *
     * @return
     */
    public static boolean isLDATA(String type) {
        if (isExpression(type))
            return true;
        else if (isNumeric(type))
            return true;
        else if (isUnit(type))
            return true;
        else return SetLogic.xISyP(Notepad.searchByTitle(type), Notepad.searchByTitle("ldata"));
    }

    public static boolean isExpression(String expression) {
        String[] parsedExpression = expression.split(" ");
        if (parsedExpression.length == 4) {
            if (!parsedExpression[1].matches("^(<|>|<=|>=|==)$"))
                return false;
            else if (!isNumeric(parsedExpression[2]))
                return false;
            return isUnit(parsedExpression[3]);
        } else if (parsedExpression.length == 3) {
            if (!parsedExpression[1].matches("^(<|>|<=|>=|==)$"))
                return false;
            else return isNumeric(parsedExpression[2]);
        }
        return false;
    }

    public static boolean isUnit(String unit) {
        Node unitNode = Notepad.searchByTitle("unit");
        for (String possibleMatch : Node.getCarrot(unitNode, "^unit")) {
            if (possibleMatch.equals(unit))
                return true;
        }
        return false;
    }

    public static boolean isUnitsEqual(Node a, Node b) {
        if (Node.contains(a, "unit") && Node.contains(b, "unit"))
            //noinspection ConstantConditions
            return Node.get(a, "unit").equals(Node.get(b, "unit"));
        else
            return false;
    }

    /**
     * accounts for periods
     *
     * @param str 12.0 - true, 12 - true, 123.32 - true, .12 false, 123. - false
     *
     * @return true is String is Numeric by the above standards, false if string is not numeric.
     */
    public static boolean isNumeric(String str) {
        return str.matches("^[-+]?\\d+(\\.\\d+)?$");
    }
}
