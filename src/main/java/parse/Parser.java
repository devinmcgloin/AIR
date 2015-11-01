package parse;

import funct.Formatter;
import logic.SetLogic;
import memory.Notepad;
import molecule.Molecule;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import static funct.Const.*;

/**
 * @author devinmcgloin
 * @version 10/5/15.
 */
public class Parser {

    /**
     * If I pass in the "Height" node from RN i need to know the values that go under it will be string representable.
     * This is important in the construction of the ghost tree (which is responsible for searches and adding and
     * deleting.
     * <p>
     * I need it because if I get to a Key and I see it has no Value I need to handle creating either a GhostValue (a CI
     * on a range of height) or if I need to create a GhostOF node (which is a LC of the Key). Those two are handled
     * entirely differently.
     *
     * @param key
     * @return
     */
    public static Predicate<Node> isKeyStringRepresentable = key ->
            Node.getCarrot(key, LOGICAL_P.toString()).stream()
                    .anyMatch(s -> s.equals(STRING_REP.toString()));
    /**
     *
     */
    public static Predicate<String> isUnit = unit -> {
        Node unitNode = Notepad.searchByTitle("unit");
        for (String possibleMatch : Node.getCarrot(unitNode, "^unit")) {
            if (possibleMatch.equals(unit))
                return true;
        }
        return false;
    };

    public static Predicate<String> existsInDB= name -> Notepad.search(name) != null;
    /**
     * accounts for periods
     *
     * @param str 12.0 - true, 12 - true, 123.32 - true, .12 false, 123. - false
     * @return true is String is Numeric by the above standards, false if string is not numeric.
     */
    public static Predicate<String> isNumeric = str -> str.matches("^[-+]?\\d+(\\.\\d+)?$");
    public static Predicate<String> isCount = count -> isNumeric.apply(count);
    public static Predicate<String> isMeasurement = o -> {
        String[] splitMeasuremnt = o.split(" ");
        if (splitMeasuremnt.length != 2) {
            return false;
        }
        return isCount.apply(splitMeasuremnt[0]) && isUnit.apply(splitMeasuremnt[1]);
    };
    public static Predicate<String> isExpression = expression -> {
        String[] parsedExpression = expression.split(" ");
        if (parsedExpression.length == 4) {
            if (!parsedExpression[1].matches("^(<|>|<=|>=|==)$"))
                return false;
            else if (!isNumeric.apply(parsedExpression[2]))
                return false;
            return isUnit.apply(parsedExpression[3]);
        } else if (parsedExpression.length == 3) {
            if (!parsedExpression[1].matches("^(<|>|<=|>=|==)$"))
                return false;
            else return isNumeric.apply(parsedExpression[2]);
        }
        return false;
    };
    /**
     * This is far easier for Devin than the previous idea: isKeyStringRepresentatble(Node key)     here you would
     * submit "height" as a node to see that it takes a string rep. Wait no i still need that function done.
     *
     * @param strRep
     * @return
     */
    public static Predicate<String> isStringRepresentation = o -> isCount.apply(o) || isExpression.apply(o) || isMeasurement.apply(o);
    /**
     * Takes a type of ldata, but also a string of ldata values.
     *
     * @param type
     * @return
     */
    public static Predicate<String> isLDATA = o -> {
        if (isExpression.apply(o))
            return true;
        else if (isNumeric.apply(o))
            return true;
        else if (isUnit.apply(o))
            return true;
        else return SetLogic.xISyP(Notepad.searchByTitle(o), Notepad.searchByTitle("ldata"));
    };

    static Logger logger = Logger.getLogger(Parser.class);

    public static boolean isExpression(Node expression) {
        return Node.getTitle(expression).equals(EXPRESSION.toString());
    }

    public static boolean isLDATA(Node expression) {
        return SetLogic.xISyP(expression, Notepad.searchByTitle("ldata"));
    }

    public static Molecule parseDefault(String msg) {
        String[] split = msg.split(" ");












    return null;
    }

    private static Node getNode(String nodeName) {
        Node n = Notepad.search(nodeName);
        if (n == null) {
            if (isStringRepresentation.apply(nodeName)) {
                return getStringRep(nodeName);
            } else return PA.createNode(nodeName);
        } else return n;
    }

    /**
     * String reps work for the following: "1283", "123.123", "1229 cm", "height > 12 ft"
     *
     * @param strRep
     *
     * @return
     */
    public static Node getStringRep(String strRep) {
        if (isMeasurement.apply(strRep))
            return getMeasurement(strRep);
        else if (isCount.apply(strRep))
            return getCount(strRep);
        else if (isExpression.apply(strRep))
            return getExpression(strRep);
        else return null;
    }

    public static Node getExpression(String expression) {
        if (isExpression.apply(expression)) {
            Node template = Notepad.searchByTitle("expression");
            String[] parsedExpression = expression.split(" ");
            if (parsedExpression.length == 3) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                template = Node.add(template, STRING_REP.toString(), expression);
                return template;
            } else if (parsedExpression.length == 4) {
                template = Node.add(template, "type", parsedExpression[0]);
                template = Node.add(template, "operator", parsedExpression[1]);
                template = Node.add(template, "value", parsedExpression[2]);
                template = Node.add(template, "unit", parsedExpression[3]);
                template = Node.add(template, STRING_REP.toString(), expression);
                return template;
            } else return null;
        } else return null;
    }

    public static Node getCount(String count) {
        if (isCount.apply(count)) {
            Node template = Notepad.searchByTitle("number");
            template = Node.add(template, "#", count);
            template = Node.add(template, STRING_REP.toString(), count);
            logger.debug(Formatter.viewNode(template));
            return template;
        }
        return null;
    }

    public static Node getMeasurement(String measure) {
        if (isMeasurement.apply(measure)) {
            String[] splitMeasuremnt = measure.split(" ");
            Node template = Notepad.searchByTitle("measurement");
            template = Node.add(template, "#", splitMeasuremnt[0]);
            template = Node.add(template, "unit", splitMeasuremnt[1]);
            template = Node.add(template, STRING_REP.toString(), measure);
            return template;
        }
        return null;
    }

    public static boolean isDefault(String msg) {
        String[] split = msg.split(" ");
        for(String s : split){
            if(s.equals("has") || s.equals("is") || s.equals("like"))
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

}
