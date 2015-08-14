package pa;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/14/15.
 */
public final class ExecutionFlow {
    private final Method method;
    private ArrayList<Object> arguments;
    private boolean doneP = false;

    public ExecutionFlow(Method method){
        this.method = method;
    }

    public void invoke(){

    }
}
