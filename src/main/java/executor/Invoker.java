package executor;

import molecule.Molecule;
import org.apache.log4j.Logger;
import pa.Node;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/5/15.
 */
class Invoker {
    static Logger logger = Logger.getLogger(Invoker.class);
    static MethodResolver methodResolver = new MethodResolver();

    public static void doThis(Molecule m) {
        Method method = methodResolver.resolve(m);
        logger.debug(method.toString());
//        invoke(method, )
    }

    protected static ExecutionFlow invoke(Method m, ArrayList<Node> argumentID) {
        ExecutionFlow flow = new ExecutionFlow(m);

        return null;
    }
}
