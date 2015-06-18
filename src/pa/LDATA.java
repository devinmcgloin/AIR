package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/17/15.
 */
public class LDATA {

    LDBN currentLDBN;
    PA pa;

    /**
     * this needs to go through an index Nouns on startup to make sure it has a copy of all the nouns that have an LDATA flag.
     * and pull out evaluation metrics, ranges and conversion methods.
     */
    public LDATA(PA pa){
        this.pa = pa;
    }
    /**
     * TODO make this have real logic.
     * Supports <=, >=, <, >, and ==
     * Currently only works on numbers. No true logical state types.
     * @param expression - take the following form: post_office == small.
     * @param node
     * @return
     */
    public boolean evaluate(String expression, NBN node){
        String [] terms = expression.split(" ");
        String attribute = terms[0]; //height
        String operator = terms[1]; // <
        String qualifier = terms[2]; // 1000000_ft

        currentLDBN = pa.getLDATA(attribute);

        if(node.hasFilter(attribute)){
            ArrayList<String> nodeVal = node.getOrigin().getChild("^has").getChild(attribute).getChildrenString();

            if(currentLDBN.getComp().equals("ordered")) {
                for (String value : nodeVal) {

                    //TODO weird split needed here, else expression will split units too. See line 29.
                    String[] qualifiers = qualifier.split("_");
                    String[] values = value.split(" ");

                    if (!values[1].equals(qualifiers[1])) {
                        values = conversion(qualifiers[1], values[1], values[0], currentLDBN);
                    }

                    //TODO good place to asses where the information will be compared and with what logic.
                    if (switchBoard(operator, values[0], qualifiers[0]))
                        return true;
                }
            }else{
                //TODO Implement validation for non ordered logic.
            }
        }else
            return false;
        return false;
    }

    /**
     * Only can be used for ordered numerical data.
     * @param operator
     * @param nodeVal
     * @param qualifier
     * @return
     */
    private boolean switchBoard(String operator, String nodeVal, String qualifier){
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

    /**
     * Conversions are stores in LDATA as conversion factors. In which the first number is a orperator, and the second the factor.
     * It can do more than one of these.
     * @param unitTo - unit converting to
     * @param value - ammount and unit coming from sperated by spaces
     * @param ldataInfo - thing the value is about
     * @return  new values with units.
     */
    private String [] conversion(String unitTo, String unitFrom, String value, LDBN ldataInfo){

        //select proper conversion option

        String conversionFactors = ldataInfo.getConversion(unitFrom, unitTo);

        //parse the conversion factor for steps to conversion.
        String[] conversionSteps = conversionFactors.split(" ");
        double num = Double.valueOf(value.trim());

        for(int i = 0; i < conversionSteps.length; i += 2){
            if(conversionSteps[i].equals("*"))
                num = num * Double.valueOf(conversionSteps[i+1]);
        }

        return new String [] {String.valueOf(num),unitTo};
    }
}
