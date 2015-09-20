package output;

import funct.Core;
import memory.Whiteboard;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;
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
    private final String HELP_STRING = "REPL Help:\n\n" +
            "\nCommands:\n" +
            "You must reference commands by their full path name. \nIf you want to create a node: pa.PA.createNode \nNotice that capitalization is important. \n\nOnly arguments that are aceeptable are Strings and Nodes. \nNodes have a @ sign before their title. Strings are surrounded in quotes. @esb \"Height\"";
    private final String NODE_ID = "@";
    private Scanner input = new Scanner(System.in);

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
                    //TODO come back and QA this.
                    for(String id : argumentID){
                        if (id.startsWith(NODE_ID))
                            flow.applyArgument(Whiteboard.searchByTitle(id.replace("@", "")));
                        else if (id.startsWith("\"") || id.endsWith("\""))
                            flow.applyArgument(id.replace("\"", ""));
                        else
                            flow.applyArgument(Whiteboard.search(id));
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

        }catch(ClassNotFoundException e){
            logger.error("Class not found...");
        }
        return null;
    }

    private returnTuple parseCommand(String command){
        String[] terms = command.split(" ");

        //Have the full class and method name
        if(terms[0].contains(".")){
            int firstPeriod = command.indexOf(".");
            int period = command.indexOf(".", firstPeriod + 1);

            String className = command.substring(0, period);
            String methodName = command.substring(period + 1, command.indexOf(" ")).trim();
            String everythingElse = command.substring(command.indexOf(" ") + 1, command.length());

            ArrayList<String> arguments = new ArrayList<>();
            for(String term : everythingElse.split(",")){
                arguments.add(term.trim());
            }
            return new returnTuple(className, methodName, arguments);
        }else{
            //TODO Need to account for LDATA node modifications as IS/HAS relationships don't apply.
            switch (terms[1]) {
                case "has":
                    command = command.replace("has", ",");
                    command = "Highlevel.has" + command;
                    command += command;
                    return parseCommand(command);
                case "is":
                    command = command.replace("is", ",");
                    command = "Highlevel.is" + command;
                    command += command;
                    return parseCommand(command);
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
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
            return;
        } else if (command.toLowerCase().equals("HELP_STRING") || command.toLowerCase().equals("?")) {
            Core.println(HELP_STRING);
        } else if (command.toLowerCase().equals("end")) {
            Core.println("Ending this conversation");
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
        }else {
            //TODO return things back to the whiteboard. Technically we can ignore the things these funcitons return as they will be placed on the whiteboard.
            returnTuple parsedCommands = parseCommand(command);
            ExecutionFlow returnedObject = null;
            if (parsedCommands != null) {
                returnedObject = invoke(parsedCommands.getFirst(), parsedCommands.getSecond(), parsedCommands.getThird());
            }

            if (returnedObject != null && returnedObject.completedP()) {
                if (returnedObject.getResult() instanceof Node)
                    Whiteboard.addNode((Node) returnedObject.getResult());
                else if (returnedObject.getResult() instanceof String)
                    System.out.println(returnedObject.getResult());
            }
        }
        Whiteboard.cycle();
        cycle();
    }


}
