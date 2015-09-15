package funct;

import logic.LDATA;
import pa.Node;
import pa.PA;

/**
 * Created by devinmcgloin on 8/25/15.
 * TODO for translating string representations from strings into nodes and back again.
 */
public class StringRepresentation {

    /**
     * String reps work for the following: "1283", "123.123", "1229 cm", "height > 12 ft"
     *
     * @param strRep
     * @return
     */
    public static Node getStringRep(String strRep) {

    }

    public static boolean isStrRep(String strRep) {
        return isCount(strRep) || isExpression(strRep) || isLdata(strRep);
    }

    public static Node getExpression(String expression) {
        Node template = PA.searchExactTitle("expression");
        String[] parsedExpression = expression.split(" ");

    }

    public static boolean isExpression(String expression) {
        return LDATA.isExpression(expression);
    }

    public static Node getCount(String count) {
        if (isCount(count)) {
            Node template = PA.searchExactTitle("number");
            template = Node.add(template, "^#", count);
            return template;
        }
    }

    public static boolean isCount(String count) {
        return LDATA.isNumeric(count);
    }

    public static Node getLdata(String ldataVal) {

    }

    public static boolean isLdata(String ldataVal) {

    }
}
