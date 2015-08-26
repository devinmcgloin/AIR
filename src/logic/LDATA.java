package logic;

import memory.Whiteboard;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/17/15.
 * GENERAL NOTES
 * TODO not sure about how to represent non nnumerical data, (time, geo, etc) with expressions and may just bypass it altogether.
 * TODO Need to QA everything
 * Default return value is true.
 *
 * TODO add structured addition and removal functions as well as strucutred getCarrot.
 */
public final class LDATA {

    static Logger logger = Logger.getLogger(LDATA.class);


    /**
     *
     */
    private LDATA(){}

    /**
     *
     * @param metric
     * @param A
     * @param B
     * @return
     */
    public static int compareBy(String metric, Node A, Node B) {

    }


    /**
     * Uses the other evaluateP function to do logic comparisons, this just goes through and gets the proper value out of the Node.
     * K:V pair values can be either a:
     *      LData Value
     *      Ans to Set (Node)
     *      # of KEY
     *      Overflown Node (not an ans, at all) - search takes care of this case.
     *      Blank - Search should also never return a blank value.
     *
     *
     * @param node
     * @param value
     * @return
     */
    public static boolean validateP(Node node, Node value) {

    }

    public static boolean validateP(String node, String value) {

    }

    /**
     *
     * @param initialValue
     * @param unitTo
     * @return
     */
    public static String convert(Node initialValue, String unitTo) {

    }

    /**
     * Takes the unit and returns the ldata node associated with it
     * @param unit
     * @return
     */
    public static Node getType(String unit) {

    }

    /**
     * Takes a type of ldata, but also a string of ldata values.
     * @param type
     * @return
     */
    public static boolean ldataP(String type){

    }

    /**
     * accounts for periods
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("(\\d+|\\.{1})");
    }

    /**
     * @param node
     * @param key
     * @param unit
     * @return
     */
    public static double getCastConvert(Node node, String key, String unit) {

    }

    /**
     *
     * @param node
     * @param key
     * @return
     */
    public static double getCast(Node node, String key) {

    }

    /**
     *
     * @param node
     * @param convertTitle
     * @param convertSteps
     * @return
     */
    public static Node addConversion(Node node, String convertTitle, String convertSteps){

    }

    /**
     *
     * @param node
     * @param valRange
     * @return
     */
    public static Node addValRange(Node node, Node valRange) {
        String strRep = Node.getStringRep(valRange);
        node = Node.add(node, "^expression", strRep);
        Whiteboard.addNode(node);
        return node;
    }

    public static ArrayList<Node> getValRange(Node node) {
        ArrayList<String> valRanges = Node.getCarrot(node, "^expression");
        ArrayList<Node> expressions = new ArrayList<>();
        for (String expression : valRanges) {
            String[] parsedExpression = expression.split(" ");
            if (parsedExpression.length == 3) {
                Node tmp = PA.getByExactTitle("expression");
                tmp = Node.add(tmp, "operator", parsedExpression[0]);
                tmp = Node.add(tmp, "value", parsedExpression[1]);
                tmp = Node.add(tmp, "unit", parsedExpression[2]);
                expressions.add(tmp);
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
    public static Node addUnit(Node node, String key){
        Node unitNode = getUnits(node);
        unitNode = Node.add(unitNode, "^unit", key);
        Whiteboard.addNode(unitNode);
        return unitNode;
    }

    /**
     * @param ldataType
     * @return the height^unit node.
     */
    public static Node getUnits(Node ldataType) {
        ArrayList<Node> unitNodes = Search.overflowSearch(ldataType, "^unit");
        if (unitNodes.size() == 1) {
            Node unitNode = unitNodes.get(0);
            return unitNode;
        } else return null;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String unitScaling(Node value) {

    }

    private static String getConversionFactors(Node node, String unitTo) {
        Node unitNode = getUnits(node);
        for (String conversion : Node.getCarrot(unitNode, "^conversion")) {
            String[] parsedConversion = conversion.split("->");
            String currentUnit = Node.get(node, "unit");
            if (parsedConversion[0].equals(currentUnit) && parsedConversion[1].equals(unitTo)) {
                return parsedConversion[2];
            }
        }
        return null;
    }

    public static Node setValue(Node node, double value) {
        Node tmp = Node.update(node, "#", Node.get(node, "#"), Double.toString(value));
        Whiteboard.addNode(tmp);
        return tmp;
    }
}