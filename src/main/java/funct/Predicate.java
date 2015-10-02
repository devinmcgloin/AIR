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
}
