package logic;

import funct.Predicate;
import memory.Notepad;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Optional;

/**
 * GENERAL NOTES
 * Question not sure about how to represent non numerical data, (time, geo, etc) with expressions and may just bypass it altogether.
 * Default return value is true.
 * <p/>
 * implement add structured addition and removal functions as well as strucutred getCarrot for LDATA nodes
 * implement functionality that allows manpulation of ldata expressions in node form.
 *
 * @author devinmcgloin
 * @version 6/17/15.
 */
public final class LDATA {

    static Logger logger = Logger.getLogger(LDATA.class);


    /**
     *
     */
    private LDATA() {
    }


    /**
     * Uses the other evaluateP function to do logic comparisons, this just goes through and gets the proper value out of the Node.
     * K:V pair values can be either a:
     * LData Value
     * Ans to Set (Node)
     * # of KEY
     * Overflown Node (not an ans, at all) - search takes care of this case.
     * Blank - Search should also never return a blank value.
     * <p/>
     * TODO This is used to verify that a expression node works for the given node.
     * TODO Take multiple params and run it thru blaze search.
     * <p/>
     * Implement is valid for nodes in ldata
     * <p/>
     * Needs to iterate over value ranges, and instantiate the strings as expressions / or maybe using a high level get.
     *
     * @param node        node that the expression is being compared to.
     * @param expression  Expression node
     * @return
     */
    public static boolean expressionIsValid(Node node, Node expression) {

        if (!Predicate.isExpression(expression)) {
            return true;
        }

        //First try to validate based on the node passed in, if the key is present then that is the deciding factor. Otherwise find where the key would go and call the function again.
        Node expressionType = Notepad.searchByTitle(Node.get(expression, "type"));
        if (!SetLogic.xISyP(expressionType, Notepad.searchByTitle("ldata"))) {
            return true;
        }
        //Q: are options prioritized by how close they are to the root node? A: Yes they are
        //TODO make sure seach is returning the values not the keys.
        //TODO need to ensure that the options are measuremnts or counts.
        ArrayList<Node> options = Scribe.searchHighLevelValues(node, expressionType);
        for (Node option : options) {
            //If this is every valid we return true, other wise false
            if (expressionValidate(option, expression))
                return true;
        }


        return false;

    }

    /**
     * Deals with casting, converting an comparing values. Does not deal with any sort of set logic. The nodes passed in are the nodes compared.
     *
     * TODO handle nulls
     * @param option     should be either a measurement, or a count. Nothing else should get this far
     * @param expression should apply to the option node, if it does not it will return true.
     * @return
     */
    private static boolean expressionValidate(Node option, Node expression) {
        if (expression == null || option == null) {
            return false;
        }

        String unit = Node.get(expression, "unit");
        double value = getCastConvert(option, unit);
        Optional<String> val = Optional.of(Node.get(expression, "value"));
        if (val.isPresent()) {
            double expressionVal = Double.valueOf(val.get());
            String operator = Node.get(expression, "operator");

            switch (operator != null ? operator : "default") {
                case "==":
                    return value == expressionVal;
                case "<=":
                    return value <= expressionVal;
                case ">=":
                    return value >= expressionVal;
                case "<":
                    return value < expressionVal;
                case ">":
                    return value > expressionVal;
                default:
                    return false;
            }
        } else return false;


    }

    /**
     * TODO This is used to verify when adding values to nodes in the highlevel add function.
     * <p/>
     * Implement isvalid in ldata
     * <p/>
     * Going to need some backend number crunching.
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean isValid(Node key, Node value) {
        Node ldata = Notepad.searchByTitle("ldata");

        if (!SetLogic.xISyP(key, ldata) || !SetLogic.xISyP(value, ldata))
            return true;


    }

    /**
     * @param initialValue - We want this to be taj mahal^height, which is basically an instatiated height node.
     * @param unitTo
     * @return
     */
    public static Node convert(Node initialValue, String unitTo) {
        Node unitNode = getUnits(initialValue); // gives you the meter node.
        String conversionStep = getConversionFactors(unitNode, unitTo);
        if (conversionStep == null) {
            logger.warn("Node cannot be converted");
            return initialValue;
        }
        String[] conversionSteps = conversionStep.split(" ");

        //Question this seems to be redundant as its already done in conversionsteps. It returns the new spec. Need to check notes at home. QA this.

        String oldVal = Double.toString(getCast(initialValue));
        String oldUnit = Node.get(initialValue, "unit");
        double num = getCast(initialValue);

        for (int i = 2; i < conversionSteps.length; i += 2) {
            switch (conversionSteps[i]) {
                case "*":
                    num = num * Double.valueOf(conversionSteps[i + 1]);
                    break;
                case "/":
                    num = num / Double.valueOf(conversionSteps[i + 1]);
                    break;
                case "+":
                    num = num + Double.valueOf(conversionSteps[i + 1]);
                    break;
                case "-":
                    num = num - Double.valueOf(conversionSteps[i + 1]);
                    break;
            }
        }
        initialValue = Node.update(initialValue, "#", oldVal, Double.toString(num));
        initialValue = Node.update(initialValue, "unit", oldUnit, unitTo);
        Notepad.addNode(initialValue);
        return initialValue;
    }

    /**
     * Takes the unit and returns the ldata node associated with it
     *
     * @param unit
     * @return
     */
    public static Node getType(String unit) {
        return SetLogic.getLogicalParents(PA.searchExactTitle(unit));
    }

    /**
     * Takes the unit and returns the ldata node associated with it
     *
     * @param node
     * @return
     */
    public static Node getType(Node node) {
        return SetLogic.getLogicalParents(node);
    }

    /**
     * Takes a type of ldata, but also a string of ldata values.
     *
     * @param type
     * @return
     */
    public static boolean isLdata(String type) {
        if (isExpression(type))
            return true;
        else if (isNumeric(type))
            return true;
        else if (isUnit(type))
            return true;
        else return SetLogic.xISyP(PA.searchExactTitle(type), PA.searchExactTitle("ldata"));
    }

    public static boolean isExpression(String expression) {
        String[] parsedExpression = expression.split(" ");
        if (parsedExpression.length == 4) {
            if (!parsedExpression[1].matches("^(<|>|=<|=>|==)$"))
                return false;
            else if (!isNumeric(parsedExpression[2]))
                return false;
            return isUnit(parsedExpression[3]);
        } else if (parsedExpression.length == 3) {
            if (!parsedExpression[1].matches("^(<|>|=<|=>|==)$"))
                return false;
            else return isNumeric(parsedExpression[2]);
        }
        return false;
    }

    public static boolean isUnit(String unit) {
        Node unitNode = PA.searchExactTitle("unit");
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
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    /**
     * @param node
     * @param unit
     * @return
     */
    public static double getCastConvert(Node node, String unit) {
        node = convert(node, unit);
        return getCast(node);
    }

    /**
     * @param node
     * @return
     */
    public static double getCast(Node node) {
        if (Node.contains(node, "#"))
            return Double.parseDouble(Node.get(node, "#"));
        else
            return -1;
    }

    /**
     * @param node
     * @param convertTitle
     * @param convertSteps
     * @return
     */
    public static Node addConversion(Node node, String convertTitle, String convertSteps) {
        Node unitNode = getUnits(node);
        unitNode = Node.add(unitNode, "^conversion", convertTitle + "->" + convertSteps);
        Notepad.addNode(unitNode);
        return unitNode;
    }

    /**
     * @param node
     * @param valRange
     * @return
     */
    public static Node addValRange(Node node, Node valRange) {
        String strRep = Node.getStringRep(valRange);
        node = Node.add(node, "^expression", strRep);
        Notepad.addNode(node);
        return node;
    }

    public static ArrayList<Node> getValRange(Node node) {
        ArrayList<String> valRanges = Node.getCarrot(node, "^expression");
        ArrayList<Node> expressions = new ArrayList<>();
        for (String expression : valRanges) {
            String[] parsedExpression = expression.split(" ");
            if (parsedExpression.length == 3) {
                Node tmp = PA.searchExactTitle("expression");
                tmp = Node.add(tmp, "operator", parsedExpression[0]);
                tmp = Node.add(tmp, "value", parsedExpression[1]);
                tmp = Node.add(tmp, "unit", parsedExpression[2]);
                expressions.add(tmp);
            } else {
                logger.warn("Expression: " + expression + " is not a valid expression.");
            }

        }

        if (expressions.size() == 0)
            return null;

        return expressions;
    }

    /**
     * Adds units to the overflow node for that nodes units.
     *
     * @param node
     * @param key
     * @return
     */
    public static Node addUnit(Node node, String key) {
        Node unitNode = getUnits(node);
        unitNode = Node.add(unitNode, "^unit", key);
        Notepad.addNode(unitNode);
        return unitNode;
    }

    /**
     * @param ldataType
     * @return the height^unit node.
     */
    public static Node getUnits(Node ldataType) {
        ArrayList<Node> unitNodes = Search.overflowSearch(ldataType, "^unit");
        if (unitNodes.size() == 1) {
            return unitNodes.get(0);
        } else return null;
    }

//    /**
//     * implement unit scaling
//     * @param value
//     * @return
//     */
//    public static Node unitScaling(Node value) {
//
//    }

    private static String getConversionFactors(Node node, String unitTo) {
        Node unitNode = getType(node);
        for (String conversion : Node.getCarrot(unitNode, "^conversion")) {
            String[] parsedConversion = conversion.split("->");
            String currentUnit = Node.get(node, "unit");
            if (parsedConversion[0].equals(currentUnit) && parsedConversion[1].equals(unitTo)) {
                return parsedConversion[2]; //
            }
        }
        return null;
    }

    public static Node addValue(Node node, double value) {
        Node tmp = Node.update(node, "#", Node.get(node, "#"), Double.toString(value));
        Notepad.addNode(tmp);
        return tmp;
    }

    public static Node addValue(Node node, String value) {
        Node tmp = Node.update(node, "#", Node.get(node, "#"), value);
        Notepad.addNode(tmp);
        return tmp;
    }

    private static int compare(double a, double b) {
        if (a == b)
            return 0;
        else if (a > b)
            return -1;
        else
            return 1;
    }
}
