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
            "\nCommands:" +
            "( help | ? ) - [SOLO] help text" +
            "(     q    ) - [SOLO] quit the program" +
            "(    end   ) - [SOLO] end the current converstation. Clears whiteboard and puts nodes." +
            "(   like   ) - [INFIX] triggers xLikey" +
            "(    is    ) - [INFIX] triggers xINHERITy" +
            "(  create  ) - [PREFIX] creates a new node with default carrot headers" +
            "(   view   ) - [PREFIX] prints the content of the node to stnd.out" +
            "(   add    ) - [PREFIX] triggers Node.add" +
            "(  called  ) - [INFIX] adds name to the specified node" +
            "\n" +
            "String arguments must be surrounded by quotation marks. - used in the add function." +
            "Nodes are referenced by name.";
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
                        if (id.startsWith("\"") && id.endsWith("\""))
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

    private returnTuple parseFull(String command) {
        //Have the full class and method name
        int firstPeriod = command.indexOf(".");
        int period = command.indexOf(".", firstPeriod + 1);

        String className = command.substring(0, period);
        String methodName = command.substring(period + 1, command.indexOf(" ")).trim();
        String everythingElse = command.substring(command.indexOf(" ") + 1, command.length());

        ArrayList<String> arguments = new ArrayList<>();
        for (String term : everythingElse.split(",")) {
            arguments.add(term.trim());
        }
        return new returnTuple(className, methodName, arguments);
    }


    public String formatNodes(ArrayList<Node> items) {
        StringBuilder output = new StringBuilder();
        for (Node node : items) {
            output.append(" ").append(node.toString()).append(" ,");
        }
        return output.toString();
    }

    private String viewNode(Node n) {
        StringBuilder stringBuilder = new StringBuilder();
        if (n != null) {
            int depth = 1;
            stringBuilder.append(stringSpacer(depth)).append(Node.getTitle(n)).append("\n");
            for (String kid : Node.getKeys(n)) {
                if (kid.startsWith("^")) {
                    stringBuilder.append(stringSpacer(depth * 4)).append("├── ").append(kid).append("\n");
                    for (String kidKid : Node.getCarrot(n, kid)) {
                        stringBuilder.append(stringSpacer(depth * 8)).append("├── ").append(kidKid).append("\n");

                    }
                } else {
                    stringBuilder.append(stringSpacer(depth * 4)).append("├── ").append(kid).append("\n");
                    String kidKid = Node.get(n, kid);
                    if (kidKid != null)
                        stringBuilder.append(stringSpacer(depth * 8)).append("├── ").append(kidKid).append("\n");

                }
            }
        } else {
            return "";
        }
        return stringBuilder.toString();
    }

    private String stringSpacer(int i) {
        String returnString = "";
        for (int j = 0; j < i; j++) {
            returnString += " ";
        }
        return returnString;
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
        } else if (command.toLowerCase().equals("help") || command.toLowerCase().equals("?")) {
            Core.println(HELP_STRING);
        } else if (command.toLowerCase().equals("end")) {
            Core.println("Ending this conversation");
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
        }else {
            String[] terms = command.split(" ");
            returnTuple parsedCommands = null;
            //Have the full class and method name


            if (terms.length == 1) {
                Whiteboard.search(terms[0]);
            } else if (terms[0].contains(".") && terms.length > 1) {
                parsedCommands = parseFull(command);
            } else if (Core.contains(terms, "like") || Core.contains(terms, "is") || Core.contains(terms, "called")) {
                //Infix Notation
                if (Core.contains(terms, "like")) {
                    command = command.replace("like", ",");
                    command = "logic.SetLogic.xLikey " + command;
                    parsedCommands = parseFull(command);
                } else if (Core.contains(terms, "is")) {
                    command = command.replace("is", ",");
                    command = "logic.SetLogic.xINHERITy " + command;
                    parsedCommands = parseFull(command);
                } else if (Core.contains(terms, "called")) {
                    String[] calledSplit = command.split("called");
                    command = "pa.Node.add " + calledSplit[0].trim() + ", \"^name\", \"" + calledSplit[1].trim() + "\"";
                    parsedCommands = parseFull(command);
                }
            } else {
                //Prefix Notation
                switch (terms[0]) {
                    case "createNode":
                        Whiteboard.addNode(PA.createNode(command.replace("create", "").trim()));
                        break;
                    case "view":
                        Core.println(viewNode(Whiteboard.search(command.replace("view", ""))));
                        break;
                    case "add":
                        command = command.replace("add", "pa.Node.add");
                        parsedCommands = parseFull(command);
                        break;
                    default:
                        cycle();
                }
            }


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
