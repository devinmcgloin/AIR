package executor;

import funct.Core;
import funct.StrRep;
import memory.Notepad;
import molecule.Molecule;
import org.apache.log4j.Logger;

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
        Core.println(method.toString());

    }

    protected static ExecutionFlow invoke(Method m, ArrayList<String> argumentID) {

        ExecutionFlow flow = new ExecutionFlow(m);
        for (String id : argumentID) {
            if (id.startsWith("\"") && id.endsWith("\""))
                flow.applyArgument(id.replace("\"", ""));
            else if (id.startsWith("~"))
                flow.applyArgument(StrRep.getStringRep(id.replace("~", "")));
            else
                flow.applyArgument(Notepad.search(id));
        }
        if (flow.appliedP())
            flow.invoke();
        if (flow.completedP()) {
            logger.info("Method executed");
            return flow;
        } else {
            logger.error("Method not executed");
        }
        return null;
    }
}
