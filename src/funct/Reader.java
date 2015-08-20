package funct;

import org.apache.log4j.Logger;
import util.Expression;

/**
 * Created by devinmcgloin on 8/4/15.
 */
public final class Reader {

    static Logger logger = Logger.getLogger(Reader.class);


    private Reader(){}

    public static Expression parseExpression(String expression){
        String[] terms = expression.trim().split(" ");
        // [ 12 - 324 ft ]
        if(expression.startsWith("[")) {
            if (terms.length == 5)
                return new Expression(terms[0], terms[1], terms[2], terms[3]);
            else if (terms.length == 4)
                return new Expression(terms[0], terms[1], terms[2], "count");
        }
        // length < 25 ft
        else{
            if(terms.length == 4)
                return new Expression(terms[0], terms[1], terms[2], terms[3]);
            else if(terms.length == 3)
                return new Expression(terms[0], terms[1], terms[2], "count");
        }
        return null;
    }

}
