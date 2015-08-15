package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/17/15.
 * GENERAL NOTES
 * TODO not sure about how to represent non nnumerical data, (time, geo, etc) with expressions and may just bypass it altogether.
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
    public static boolean validateP(Expression expression, NBN node){
        String value = Noun.search(node, expression.getType());
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

    /**
     *
     * TODO: Need to deal with infinity here
     * TODO: Need to deal with no units here. EG counts
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
     * TODO QA calling get on the returned values without checking. Alternative is using get units on returned ldbns
     * @param value
     * @return
     */
    public static LDBN getType(String value) {
        return PA.ldataHashSearch(value.split(" ")[1]).get(0);
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
        }else{
            return false;
        }
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

    public static ArrayList<LDATA.Expression> getValRanges(LDBN type){
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

    /**
     * When comparing by count make sure to specify that the unit is count.
     */
    static class Expression {
        final String type;
        final String operator;
        final String value;
        final String unit;

        public Expression(String type, String operator, String value, String unit) {
            this.type = type;
            this.operator = operator;
            this.value = value;
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Expression{" +
                    "type='" + type + '\'' +
                    ", operator='" + operator + '\'' +
                    ", value='" + value + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public String getOperator() {
            return operator;
        }

        public String getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }
    }
}
