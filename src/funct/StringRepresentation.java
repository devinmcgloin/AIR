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
    /**
     * This is far easier for Devin than the previous idea:
     * isKeyStringRepresentatble(Node key)     here you would submit "height" as a node to see that it takes a string rep.
     * Wait no i still need that function done.
     * @param s
     * @return
     */
    public static boolean isStrRep(String strRep) {
        return isCount(strRep) || isExpression(strRep) || isLdata(strRep);
    }

    /**
     * If I pass in the "Height" node from RN i need to know the values that go under it will be string representable.
     * This is important in the construction of the ghost tree (which is responsible for searches and adding and deleting.
     *
     * I need it because if I get to a Key and I see it has no Value I need to handle creating either a GhostValue (a CI on a range
     * of height) or if I need to create a GhostOF node (which is a LC of the Key). Those two are handled entirely differently.
     *
     * @param key
     * @return
     */
    public static boolean isKeyStringRepresentable(Node key){

        return false;
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
