package pa;

import java.util.ArrayList;
import util.Expression;

/**
 * Created by devinmcgloin on 6/17/15.
 * GENERAL NOTES
 * TODO not sure about how to represent non nnumerical data, (time, geo, etc) with expressions and may just bypass it altogether.
 * TODO Need to QA everything
 */
public final class LDATA {


    /**
     *
     */
    private LDATA(){}

    /**
     *
     * @param node
     * @param val
     * @return
     */
    public static boolean validateP(LDBN node, String val){
        ArrayList<Expression> ranges = getValRanges(node);
        if(getComp(node).equals("count") || getComp(node).equals("measurement")) {
            for (Expression expression : ranges) {
                if (!numValidateP(expression, val))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Uses the other evaluateP function to do logic comparisons, this just goes through and gets the proper value out of the NBN.
     * K:V pair values can be either a:
     *      LData Value
     *      Ans to Set (NBN)
     *      # of KEY
     *      Overflown Node (not an ans, at all) - search takes care of this case.
     *      Blank - Search should also never return a blank value.
     *
     * TODO: QA with #of parameters.
     * @param expression
     * @param node
     * @return
     */
    public static boolean validateP(NBN node, Expression expression){
        String value = Noun.simpleSearch(node, expression.getType());
        if(value == null){
            return false;
        }else if(ldataP(value)){
            return numValidateP(expression, value);
        }else if(Noun.nounP(value)){
            return numValidateP(expression, value);
        }else if(isNumeric(value)){
            return numValidateP(expression, value);
        }else{
            return false;
        }
    }

    public static boolean validateP(NBN node, String expression){
        return validateP(node, Reader.parseExpression(expression));
    }

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
        LDBN type = getType(terms[1]);

        String conversionFactors = getConversion(type, terms[1], unitTo);
        String[] conversionSteps = conversionFactors.split(" ");

        double num = Double.valueOf(terms[0].trim());

        for(int i = 0; i < conversionSteps.length; i += 2){
            if(conversionSteps[i].equals("*"))
                num = num * Double.valueOf(conversionSteps[i+1]);
        }

        return String.valueOf(num) + " " + unitTo;
    }

    /**
     * Takes the unit and returns the ldata node associated with it
     * @param value
     * @return
     */
    public static LDBN getType(String value) {
        ArrayList<LDBN> nodes = PA.ldataHashSearch(value.split(" ")[1]);
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
        if(PA.getLDATA(type) != null){
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
        System.out.println("LDATA: SwitchBoard. Invalid operator.");
        return false;
    }

    public static String getConversion(LDBN type, String unitTo, String unitFrom){
        return type.getConversion(unitFrom, unitTo);
    }

    public static ArrayList<Expression> getValRanges(LDBN type){
        return type.getValRanges();
    }

    public static String getComp(LDBN type){
        return type.getComp();
    }

    public static ArrayList<String> getUnits(LDBN type){
        return type.getUnits();
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

    public static String getTitle(LDBN node){
        return node.getTitle();
    }

    public static ArrayList<String> getName(LDBN node){
        ArrayList<String> returnItem = new ArrayList<String>();
        returnItem.add(node.getTitle());
        return returnItem;
    }

    public static ArrayList<String> getKeys(LDBN node){
        return node.getKeys();
    }

    public static ArrayList<String> get(LDBN node, String key){
        return node.get(key);
    }

    public static LDBN add(LDBN node, String key){ return node.add(key); }

    public static LDBN add(LDBN node, String key, String val ){
        return node.add(key, val);
    }

    public static LDBN rm(LDBN node, String key){
        return node.rm(key);
    }

    public static LDBN rm(LDBN node, String key, String val ){
        return node.rm(key, val);
    }

    public static LDBN update(LDBN node, String key, String oldVal, String newVal){
        return node.update(key, oldVal, newVal);
    }

}
