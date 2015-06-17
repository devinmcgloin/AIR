package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/17/15.
 */
public class LDATA {

    /**
     * TODO make this have real logic.
     * Supports <=, >=, <, >, and ==
     * Currently only works on numbers. No true logical state types.
     * @param expression - take the following form: post_office == small.
     * @param node
     * @return
     */
    public boolean evaluate(String expression, PABN node){
        String [] terms = expression.split(" ");
        String attribute = terms[0];
        String operator = terms[1];
        String qualifier = terms[2];

        if(node.hasFilter(attribute)){
            ArrayList<String> nodeVal = node.getOrigin().getChild("^has").getChild(attribute).getChildrenString();
            for(String value : nodeVal){
                if(switchBoard(operator, value, qualifier))
                    return true;
            }
        }else
            return false;
        return false;
    }

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
}
