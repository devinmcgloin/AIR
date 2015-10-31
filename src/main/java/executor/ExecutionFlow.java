package executor;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author devinmcgloin
 * @version 8/14/15
 */
final class ExecutionFlow {
    static Logger logger = Logger.getLogger(ExecutionFlow.class);
    private final Method method;
    private final Type[] argTypes;
    private boolean[] appliedP;
    private Object[] arguments;
    private boolean completedP = false;
    private Object result;

    public ExecutionFlow(Method method) {
        this.method = method;
        argTypes = method.getParameterTypes();
        appliedP = new boolean[argTypes.length];
        arguments = new Object[argTypes.length];
    }

    public ExecutionFlow invoke() {
        for (Object o : arguments) {
            if (o == null)
                return this;
        }
        try {
            if (argTypes.length == 1) {
                result = method.invoke(null, arguments[0]);
                completedP = true;
            } else if (argTypes.length == 2) {
                result = method.invoke(null, arguments[0], arguments[1]);
                completedP = true;
            } else if (argTypes.length == 3) {
                result = method.invoke(null, arguments[0], arguments[1], arguments[2]);
                completedP = true;
            } else if (argTypes.length == 4) {
                result = method.invoke(null, arguments[0], arguments[1],
                        arguments[2], arguments[3]);
                completedP = true;
            }
        } catch (IllegalAccessException e) {
            logger.error("EF: Illegal Access Exception");
        } catch (InvocationTargetException e) {
            logger.error("EF: Invocation Target Exception");
        }
        return this;
    }

    public void applyArgument(Object argument) {
        for (int i = 0; i < argTypes.length; i++) {
            logger.debug("Argument: " + argument.getClass().getTypeName());
            logger.debug("Method Type: " + argTypes[i].getTypeName());
            if (argTypes[i].getTypeName().equals(argument.getClass().getTypeName()) && !appliedP[i]) {
                arguments[i] = argument;
                appliedP[i] = true;
                break;
            }
        }
    }

    public Object getResult() {
        if (completedP)
            return result;
        else {
            logger.error("Computation not completed.");
            return null;
        }
    }

    public boolean completedP() {
        return completedP;
    }

    public boolean appliedP() {
        for (boolean apply : appliedP) {
            if (!apply)
                return false;
        }
        return true;
    }
}
