package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/17/15.
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
    public static boolean validateP(LDBN node, Value val){
        ArrayList<Expression> ranges = getValRanges(node);
        for(Expression expression : ranges){
            if(!validateP(expression, val))
                return false;
        }
        return true;
    }

    /**
     * Uses the other evaluateP function to do logic comparisons, this just goes through and gets the proper value out of the NBN.
     * TODO:implement after NBN can return a value expression
     * @param expression
     * @param node
     * @return
     */
    public static boolean validateP(Expression expression, NBN node){

    }

    /**
     *
     * TODO: Need to deal with infinity here
     * @param expression
     * @param val
     * @return
     */
    public static boolean validateP(Expression expression, Value val){
        if(expression.getUnit().equals(val.getUnit())){
            //no conversion needed
            return comp(val.getValue(), expression.getOperator(), expression.getValue());
        }else {
            //conversion has to happen
            Value convertedVal = convert(val, expression.getUnit());
            return comp(convertedVal.getValue(), expression.getOperator(), expression.getValue());
        }
    }


    /**
     *
     * @param initialValue
     * @param unitTo
     * @return
     */
    public static Value convert(Value initialValue, String unitTo){
        LDBN type = PA.getLDATA(initialValue.getType());

        String conversionFactors = getConversion(type, initialValue.getUnit(), unitTo);
        String[] conversionSteps = conversionFactors.split(" ");

        double num = Double.valueOf(initialValue.getValue().trim());

        for(int i = 0; i < conversionSteps.length; i += 2){
            if(conversionSteps[i].equals("*"))
                num = num * Double.valueOf(conversionSteps[i+1]);
        }

        return new Value(initialValue.getType(), String.valueOf(num), unitTo);
    }

    public static LDBN getType(Value value){
        return PA.getLDATA(value.getType());
    }

    public static ArrayList<String> getUnits(LDBN type){
        return type.getUnits();
    }

    /**
     *
     * @param type
     * @return
     */
    public static boolean ldataP(String type){
        if(PA.getLDATA(type) != null)
            return true;
        else
            return false;
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

    static class Value {
        final String type;
        final String value;
        final String unit;

        public Value(String type, String value, String unit) {

            this.type = type;
            this.value = value;
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public String getUnit() {
            return unit;
        }

        public String getValue() {
            return value;
        }

    }
}
