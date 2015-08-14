package pa;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Blazej on 6/17/2015.
 */
public final class REPL {

    private REPL(){}

    public static void invoke(String className, String methodName, ArrayList<String> argumentID){
        try {
            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for(Method method : methods){
                if(method.getName().equals(methodName)){
                    method.getGenericParameterTypes();
                }
            }
        }catch(ClassNotFoundException e){
            System.out.println("Class not found...");
        }
    }

}
