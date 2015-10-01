package funct;

import pa.Node;

/**
 * @author devinmcgloin
 * @version 9/29/15
 */
public class Predicate {

    public static boolean isExpression(Node expression) {
        return StrRep.isExpression(Node.getStringRep(expression));
    }
}
