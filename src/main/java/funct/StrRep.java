package funct;

import memory.Notepad;
import org.apache.log4j.Logger;
import pa.Node;

/**
 * @author devinmcgloin
 * @version 8/25/15
 */
public class StrRep {
    static Logger logger = Logger.getLogger(StrRep.class);


    /**
     * String reps work for the following: "1283", "123.123", "1229 cm", "height > 12 ft"
     *
     * @param strRep
     *
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
     * This is far easier for Devin than the previous idea: isKeyStringRepresentatble(Node key)     here you would
     * submit "height" as a node to see that it takes a string rep. Wait no i still need that function done.
     *
     * @param strRep
     *
     * @return
     */
    public static boolean isStringRepresentation(String strRep) {
        return isCount(strRep) || isExpression(strRep) || isMeasurement(strRep);
    }

    /**
     * If I pass in the "Height" node from RN i need to know the values that go under it will be string representable.
     * This is important in the construction of the ghost tree (which is responsible for searches and adding and
     * deleting.
     * <p/>
     * I need it because if I get to a Key and I see it has no Value I need to handle creating either a GhostValue (a CI
     * on a range of height) or if I need to create a GhostOF node (which is a LC of the Key). Those two are handled
     * entirely differently.
     *
     * @param key
     *
     * @return
     */
    public static boolean isKeyStringRepresentable(Node key) {
        return Node.getCarrot(key, Const.LOGICAL_P.toString()).stream()
                .anyMatch(s -> s.equals(Const.STRING_REP.toString()));
    }

    public static Node getExpression(String expression) {
        if (isExpression(expression)) {
            Node template = Notepad.searchByTitle("expression");
            String[] parsedExpression = expression.split(" ");
            if (parsedExpression.length == 3) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                template = Node.add(template, Const.STRING_REP.toString(), expression);
                return template;
            } else if (parsedExpression.length == 4) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                template = Node.add(template, "unit", parsedExpression[3]);
                template = Node.add(template, Const.STRING_REP.toString(), expression);
                return template;
            } else return null;
        } else return null;
    }

    public static boolean isExpression(String expression) {
        return Predicate.isExpression(expression);
    }

    public static Node getCount(String count) {
        if (isCount(count)) {
            Node template = Notepad.searchByTitle("number");
            template = Node.add(template, "#", count);
            template = Node.add(template, Const.STRING_REP.toString(), count);
            logger.debug(Formatter.viewNode(template));
            return template;
        }
        return null;
    }

    public static boolean isCount(String count) {
        return Predicate.isNumeric(count);
    }

    public static Node getMeasurement(String measure) {
        if (isMeasurement(measure)) {
            String[] splitMeasuremnt = measure.split(" ");
            Node template = Notepad.searchByTitle("measurement");
            template = Node.add(template, "#", splitMeasuremnt[0]);
            template = Node.add(template, "unit", splitMeasuremnt[1]);
            template = Node.add(template, Const.STRING_REP.toString(), measure);
            return template;
        }
        return null;
    }

    public static boolean isMeasurement(String measure) {
        String[] splitMeasuremnt = measure.split(" ");
        if (splitMeasuremnt.length != 2) {
            return false;
        }
        return isCount(splitMeasuremnt[0]) && Predicate.isUnit(splitMeasuremnt[1]);
    }
}
