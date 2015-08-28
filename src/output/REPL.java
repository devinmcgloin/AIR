package output;

import funct.Core;
import memory.Whiteboard;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;
import util.ExecutionFlow;
import util.returnTuple;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by @devinmcgloin on 8/17/2015.
 * TODO currently REPL only works for static library methods in which the parameters are explicit. You cannot call class methods on the objects of their class.
 *
 * TODO pull out nodes implicity when provided string arguments. The only time strings will be looked at as strings would be for manipulating content inside the node.
 *
 * TODO support sets for NBNs
 *
 */
public class REPL {
    static Logger logger = Logger.getLogger(REPL.class);

    Scanner input = new Scanner(System.in);
    String help = "REPL Help:\n\n" +
            "\nCommands:\n" +
            "(ADD    | +  ): Add node at current directory\n" +
            "(DEL    | -  ): PATH\n" +
            "(PRINT  | ls ): List directory.\n" +
            "(BACK   | .. ): Back one level.\n" +
            "(CD     | cd ): Return to root.\n" +
            "(RENAME | mv ): PATH - newName\n" +
            "(Help   | ?  ): For help.\n\n";

    public REPL(){}

    private ExecutionFlow invoke(String className, String methodName, ArrayList<String> argumentID) {
        try {

            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for(Method method : methods){
                logger.debug(method.getName() + " == " + methodName);
                logger.debug(method.getGenericParameterTypes().length + " == " + argumentID.size());
                if(method.getName().equals(methodName) && method.getGenericParameterTypes().length == argumentID.size()){
                    ExecutionFlow flow = new ExecutionFlow(method);
                    //TODO saerching thru the whiteboard.
                    for(String id : argumentID){

                    }
                    if(flow.appliedP())
                        flow.invoke();
                    if(flow.completedP()){
                        logger.info("Method executed");
                        return flow;
                    }else{
                        logger.error("Method not executed");
                    }
                    break;
                }

            }

            logger.error("Method not found...");
            logger.error("Desired: " + methodName);
        }catch(ClassNotFoundException e){
            logger.error("Class not found...");
        }
        return null;
    }

    private returnTuple parseCommand(String command){
        String[] terms = command.split(" ");

        //Have the full class and method name
        if(terms[0].contains(".")){
            int period = command.indexOf(".");

            String className = "pa." + command.substring(0, period);
            String methodName = command.substring(period + 1, command.indexOf(" ")).trim();
            String everythingElse = command.substring(command.indexOf(" ") + 1, command.length());

            ArrayList<String> arguments = new ArrayList<>();
            for(String term : everythingElse.split(",")){
                arguments.add(term.trim());
            }
            return new returnTuple(className, methodName, arguments);
        }else{
            //TODO implement infix notation here.
            switch (terms[1]) {
                default:
                    return null;
            }

        }
    }


    private Node parse(String nodeName) {
        if (nodeName.startsWith("@")) {
            return Whiteboard.searchByTitle(nodeName.replace("@", ""));
        }
        return Whiteboard.search(nodeName);
    }


    private String formatNodes(ArrayList<Node> items) {
        String output = "";
        for (Node node : items) {
            output += " " + node.toString() + " ";
        }
        return output;
    }

    public void cycle(){
        System.out.println("\n");
        System.out.println("Nodes::       " + formatNodes(Whiteboard.getProminentNodes()));
        System.out.print(">>>");
        String command = input.nextLine().trim();
        if(command.toLowerCase().equals("q")){
            return;
        } else if (command.toLowerCase().equals("help") || command.toLowerCase().equals("?")) {
            Core.println(help);
        } else if (command.toLowerCase().equals("end")) {
            Core.println("Ending this conversation");
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
        }else {
            //TODO return things back to the whiteboard.
            returnTuple parsedCommands = parseCommand(command);
            if (parsedCommands != null) {

            }
        }
        cycle();
    }


}
