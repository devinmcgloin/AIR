package executor;

import funct.StrRep;
import memory.Notepad;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/5/15.
 */
public class Invoker {
    static Logger logger = Logger.getLogger(Invoker.class);

    public static ExecutionFlow invoke(String className, String methodName, ArrayList<String> argumentID) {
        try {

            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for (Method method : methods) {
                logger.debug(method.getName() + " == " + methodName);
                logger.debug(method.getGenericParameterTypes().length + " == " + argumentID.size());
                if (method.getName().equals(methodName)
                        && method.getGenericParameterTypes().length == argumentID.size()) {
                    ExecutionFlow flow = new ExecutionFlow(method);
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
                    break;
                }

            }

        } catch (ClassNotFoundException e) {
            logger.error("Class not found...");
        }
        return null;
    }
}
