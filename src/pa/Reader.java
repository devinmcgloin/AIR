package pa;

import pa.LDATA;
import util.Expression;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/4/15.
 */
public final class Reader {

    private Reader(){}

    public static Expression parseExpression(String expression){
        String[] terms = expression.trim().split(" ");
        // [ 12 - 324 ft ]
            if(terms.length == 5)
                return new Expression(terms[0],terms[1],terms[2], terms[3] );
            else
                return new Expression(terms[0],terms[1],terms[2], "count" );

    }

}
