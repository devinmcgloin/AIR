package executor;

import memory.Notepad;
import molecule.Molecule;
import pa.Node;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * Matches molecules that Method Perceptron gets and resolves them to an actual bit of code we can execute
 * @author devinmcgloin
 * @version 10/31/15.
 */
class MethodResolver {
    HashMap<Node, Method> canDo = new HashMap<>();

    protected MethodResolver() {
        canDo.put(Notepad.searchByTitle("like"), getMethod("logic.SetLogic xLikey"));
        canDo.put(Notepad.searchByTitle("has"), getMethod("method.Scribe addHighLevel"));
//        canDo.put(Notepad.searchByTitle("is"), getMethod("logic.SetLogic xINHERITy"));
    }

    protected Method resolve(Molecule m) {
        return canDo.get(m.getVerb());
    }

    private Method getMethod(String path) {
        try {
            String[] split = path.split(" ");
            Class execution = Class.forName(split[0]);
            Method[] methods = execution.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(split[1])) {
                    return method;
                }
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }
}
