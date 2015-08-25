package pa;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by devinmcgloin on 6/17/15.
 * GENERAL NOTES
 * TODO not sure about how to represent non nnumerical data, (time, geo, etc) with expressions and may just bypass it altogether.
 * TODO Need to QA everything
 * Default return value is true.
 *
 * TODO add structured addition and removal functions as well as strucutred get.
 */
public final class LDATA {

    static Logger logger = Logger.getLogger(LDATA.class);


    /**
     *
     */
    private LDATA(){}

    private static int compare(String valA, String valB){
        String [] valueA = valA.split(" ");
        String [] valueB = valB.split(" ");
        if(valueA.length == 2 && valueB.length == 2 && valueA[0].matches("\\d+") && valueB[0].matches("\\d+")){
            if(valueA[1].equals(valueB[1])){
                if(Double.valueOf(valueA[0]) == Double.valueOf(valueB[0]))
                    return 0;
                else if(Double.valueOf(valueA[0]) < Double.valueOf(valueB[0]))
                    return -1;
                else
                    return 1;
            }else{
                String newValB = convert(valB, valueA[1]);
                if(newValB != null){
                    return compare(valA, newValB);
                }
            }
        }
        //TODO returning 0 okay here?
        return 0;
    }

    public static boolean validateP(String key, String val){
        if(val.matches("\\d+"))
            return true;
        Node node = PA.get(key);
        if(node != null){
            return validateP(node, val);
        }else
            return false;
    }
    /**
     *
     * @param node
     * @param val
     * @return
     */
    public static boolean validateP(Node node, String val){
        ArrayList<Expression> ranges = getValRanges(node);
        if(getComp(node).equals("count") || getComp(node).equals("measurement")) {
            for (Expression expression : ranges) {
                if (!numValidateP(expression, val))
                    return false;
            }
            return true;
        }
        return true;
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
     * TODO: QA
     * @param expression
     * @param node
     * @return
     */
    public static boolean validateP(Node node, Expression expression){
        String value = Noun.simpleSearch(node, expression.getType());
        if(value == null){
            return true;
        }else if(ldataP(value)){
            return numValidateP(expression, value);
        }else if(Noun.nounP(value)){
            return numValidateP(expression, value);
        }else if(isNumeric(value)){
            return numValidateP(expression, value);
        }else{
            return true;
        }
    }

//    public static boolean validateP(Node node, String expression){
//        Expression exp = Reader.parseExpression(expression);
//        if(exp != null)
//            return validateP(node, exp );
//        else {
//            logger.warn("Invalid Expression");
//            return true;
//        }
//    }

    /**
     *
     *
     * Deals with conversion for ordered types: Counts and measurements
     * Verifies that the val is acceptable given the expression.
     * @param expression
     * @param val - value is in this case a key. [value units]
     * @return
     */
    private static boolean numValidateP(Expression expression, String val){
        String [] terms = val.trim().split(" ");
        if(expression.getUnit().equals("count") && terms.length == 1){
            return comp(terms[0], expression.getOperator(), expression.getValue());
        }else if(expression.getValue().equals("inf")){
            if(expression.getOperator().equals(">")){
                return false;
            }else if(expression.getOperator().contains("=") && terms[0].equals("inf")) {
                return true;
            }else{
                return false;
            }
        }else if(expression.getUnit().equals(terms[1])){
            //no conversion needed
            return comp(terms[1], expression.getOperator(), expression.getValue());
        }else {
            //conversion has to happen
            String convertedVal = convert(val, expression.getUnit());
            return comp(convertedVal.split(" ")[1], expression.getOperator(), expression.getValue());
        }
    }


    /**
     *
     * @param initialValue
     * @param unitTo
     * @return
     */
    public static String convert(String initialValue, String unitTo){
        String [] terms = initialValue.trim().split(" ");
        Node type = getType(terms[1]);

        String conversionFactors = getConversion(type, terms[1], unitTo);
        String[] conversionSteps = conversionFactors.split(" ");

        double num = Double.valueOf(terms[0].trim());

        for(int i = 0; i < conversionSteps.length; i += 2){
            switch (conversionSteps[i]){
                case "*":
                    num = num * Double.valueOf(conversionSteps[i+1]);
                    break;
                case "/":
                    num = num / Double.valueOf(conversionSteps[i+1]);
                    break;
                case "+":
                    num = num + Double.valueOf(conversionSteps[i+1]);
                    break;
                case "-":
                    num = num - Double.valueOf(conversionSteps[i+1]);
                    break;
            }
        }

        return String.valueOf(num) + " " + unitTo;
    }

    /**
     * Takes the unit and returns the ldata node associated with it
     * @param value
     * @return
     */
    public static Node getType(String value) {
        ArrayList<Node> nodes = PA.hashSearch(value.split(" ")[1]);
        if( nodes != null)
            return nodes.get(0);
        else
            return null;
    }

    /**
     * Takes a type of ldata, but also a string of ldata values.
     * TODO: Rewrite to handle multiple forms of ldata.
     * @param type
     * @return
     */
    public static boolean ldataP(String type){
        if(PA.get(type) != null){
            return true;
        }else if(getType(type.split(" ")[1]) != null){
            return true;
        }else if(isNumeric(type)){
            return true;
        }else return false;
    }

    /**
     * Only can be used for ordered numerical data.
     * @param operator
     * @param nodeVal
     * @param qualifier
     * @return
     */
    private static boolean comp(String nodeVal, String operator, String qualifier){
        if(operator.equals("==")){
            return nodeVal.equals(qualifier);
        }
        else if(operator.equals("<")){
            return Double.valueOf(nodeVal) < Double.valueOf(qualifier);
        }
        else if(operator.equals(">")){
            return Double.valueOf(nodeVal) > Double.valueOf(qualifier);
        }
        else if(operator.equals("<=")){
            return Double.valueOf(nodeVal) <= Double.valueOf(qualifier);
        }
        else if(operator.equals(">=")){
            return Double.valueOf(nodeVal) >= Double.valueOf(qualifier);
        }
        logger.error("LDATA: SwitchBoard. Invalid operator.");
        return false;
    }

    /**
     * accounts for periods
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)){
                if(c != '.'){
                    return false;
                }
            }
        }
        return true;
    }

//    public static double getAndConvert(Node node, String key, String unit){
//
//    }

    public static Node setStorageType(Node node, String type){
        if(Node.get(node, "^storage") == null){
            node = Node.add(node, "^storage", type);
        }
        return node;
    }

    public static Node setIsParam(Node node, String param){
        if(Node.get(node, "^is") == null){
            node = Node.add(node, "^is", param);
        }
        return node;
    }

    public static Node addConversion(Node node, String convertTitle, String convertSteps){

    }

    public static Node addValRange(Node node, String valRange){

    }

    public static Node addUnit(Node node, String key){

    }

    public static ArrayList<String> getUnits(Node n){
        return Node.get(n, "^units");
    }

    public static String unitScaling(String value){

    }

    /**
     * need to check this before you can just compare the way Im doing it now in LDATA.
     *
     * @return
     */
    public static String getComp(Node TN) {
        if (Node.get(TN, "^comparison").contains("ordered")) {
            return "ordered";
        } else if (Node.get(TN, "^comparison").contains("count")) {
            return "count";
        } else {
            //TODO more complex logic containers/Time etc. Dont know how to do yet.
            return "Nothing";
        }
    }


    /**
     * TODO new LDATA conversion form meters->ft->CONVERSIONSTEPS
     * @param unitFrom
     * @param unitTo
     * @return
     */
    public static String getConversion(Node TN, String unitFrom, String unitTo) {
        List<String> conversions = Node.get(TN,"^conversions");

        for (String conversion : conversions) {
            String[] types = conversion.split("->");
            if (types[0].equals(unitFrom) && types[1].equals(unitTo)) {
                //assumes that there is only one conversion grouping and that it's in the operation postion.
                return types[2];
            }
        }
        return "Nothing";
    }

    /**
     * @return
     */
    public static ArrayList<Expression> getValRanges(Node TN) {
        ArrayList<String> children = Node.get(TN, "^value_ranges");
        if (children.size() == 0) {
            logger.error("Node: GetValRanges: No ranges.");
            return null;
        } else {
            ArrayList<Expression> expressions = new ArrayList<>();
            //assumes value is always in the operation position and that there is only one.
            for (String range : children) {
                String[] terms = range.trim().split(" ");
                // [ 12 - 324 ft ]
                if (range.startsWith("[") || range.startsWith("(")) {
                    if (terms.length == 6) {
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new Expression(Node.getTitle(TN), ">", terms[1], terms[4]));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new Expression(Node.getTitle(TN), ">=", terms[1], terms[4]));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new Expression(Node.getTitle(TN), "<", terms[3], terms[4]));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new Expression(Node.getTitle(TN), "<=", terms[3], terms[4]));
                        }
                    } else {
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new Expression(Node.getTitle(TN), ">", terms[1], "count"));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new Expression(Node.getTitle(TN), ">=", terms[1], "count"));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new Expression(Node.getTitle(TN), "<", terms[3], "count"));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new Expression(Node.getTitle(TN), "<=", terms[3], "count"));
                        }
                    }
                } else {
                    if (terms.length == 5)
                        expressions.add(new Expression(terms[0], terms[1], terms[2], terms[3]));
                    else
                        expressions.add(new Expression(terms[0], terms[1], terms[2], "count"));
                }
            }
            return expressions;
        }


    }
}