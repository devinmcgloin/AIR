package funct;

import logic.LDATA;
import pa.Node;
import pa.PA;

/**
 * Created by devinmcgloin on 8/25/15.
 */
public class StrRep {

    /**
     * String reps work for the following: "1283", "123.123", "1229 cm", "height > 12 ft"
     *
     * @param strRep
     * @return
     */
    public static Node getStringRep(String strRep) {
        if (isMeasurement(strRep))
            return getMeasurement(strRep);
        else if (isCount(strRep))
            return getCount(strRep);
        else if (isExpression(strRep))
            return getExpression(strRep);
        else return null;
    }

    /**
     * This is far easier for Devin than the previous idea:
     * isKeyStringRepresentatble(Node key)     here you would submit "height" as a node to see that it takes a string rep.
     * Wait no i still need that function done.
     *
     * @param strRep
     * @return
     */
    public static boolean isStringRepresentation(String strRep) {
        return isCount(strRep) || isExpression(strRep) || isMeasurement(strRep);
    }

    /**
     * If I pass in the "Height" node from RN i need to know the values that go under it will be string representable.
     * This is important in the construction of the ghost tree (which is responsible for searches and adding and deleting.
     * <p>
     * I need it because if I get to a Key and I see it has no Value I need to handle creating either a GhostValue (a CI on a range
     * of height) or if I need to create a GhostOF node (which is a LC of the Key). Those two are handled entirely differently.
     *
     * @param key
     * @return
     */
    public static boolean isKeyStringRepresentable(Node key) {
        return Node.getCarrot(key, "^logicalParents").stream()
                .anyMatch(s -> s.equals("string representable"));
    }

    public static Node getExpression(String expression) {
        if (isExpression(expression)) {
            Node template = PA.searchExactTitle("expression");
            String[] parsedExpression = expression.split(" ");
            if (parsedExpression.length == 3) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                return template;
            } else if (parsedExpression.length == 4) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                template = Node.add(template, "unit", parsedExpression[3]);
                template = Node.add(template, "string representation", expression);
                return template;
            } else return null;
        } else return null;
    }

    public static boolean isExpression(String expression) {
        return LDATA.isExpression(expression);
    }

    public static Node getCount(String count) {
        if (isCount(count)) {
            Node template = PA.searchExactTitle("number");
            template = Node.add(template, "#", count);
            template = Node.add(template, "string representation", count);
            return template;
        }
        return null;
    }

    public static boolean isCount(String count) {
        return LDATA.isNumeric(count);
    }

    public static Node getMeasurement(String measure) {
        if (isMeasurement(measure)) {
            String[] splitMeasuremnt = measure.split(" ");
            Node template = PA.searchExactTitle("measurement");
            template = Node.add(template, "#", splitMeasuremnt[0]);
            template = Node.add(template, "unit", splitMeasuremnt[1]);
            template = Node.add(template, "string representation", measure);
            return template;
        }
        return null;
    }

    public static boolean isMeasurement(String measure) {
        String[] splitMeasuremnt = measure.split(" ");
        if (splitMeasuremnt.length != 2) {
            return false;
        }
        return isCount(splitMeasuremnt[0]) && LDATA.isUnit(splitMeasuremnt[1]);
    }
}
