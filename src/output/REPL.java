package output;

import funct.Core;
import funct.Formatter;
import funct.StrRep;
import memory.Whiteboard;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import pa.Node;
import pa.PA;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by @devinmcgloin on 8/17/2015.
 *
 * currently REPL only works for static library methods in which the parameters are explicit. You cannot call class methods on the objects of their class.
 *
 * TODO have repl check for string representations inside arguments and pass along the instantiated nodes into the functions.
 *
 * TODO Bulk add from a file.
 * TODO arrow up to get last command
 * TODO first command is create program fails
 */
public class REPL {
    static Logger logger = Logger.getLogger(REPL.class);
    private final String HELP_STRING = "REPL Help:\n" +
            "\nCommands:\n\n" +
            "[command type] {example call}\n\n" +
            "( help | ? ) - [SOLO] help text\n" +
            "(     q    ) - [SOLO] quit the program\n" +
            "(    end   ) - [SOLO] end the current conversation. Clears whiteboard and puts nodes.\n" +
            "(   like   ) - [INFIX] triggers xLikey {motorcycle like bike}\n" +
            "(    is    ) - [INFIX] triggers xINHERITy {fruit is food}\n" +
            "(  create  ) - [PREFIX] creates a new node with default carrot headers {create fruit}\n" +
            "(   view   ) - [PREFIX] prints the content of the node to std.out {view bmw}\n" +
            "(   add    ) - [PREFIX] triggers Node.add {add bmw,\"wheel\" | add bmw,\"wheel\",\"4\"}\n" +
            "(  called  ) - [INFIX] adds name to the specified node {bmw called bimmer}\n" +
            "\n" +
            "String arguments must be surrounded by quotation marks. - used in the add function.\n" +
            "the called function and create function do not require quotation marks, they are added by the program implicitly.\n" +
            "Nodes are referenced by name.\n";
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

    private Triplet<String, String, ArrayList<String>> parseFull(String command) {
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
        return new Triplet<>(className, methodName, arguments);
    }


    public boolean cycle() {
        System.out.println("\n");
        System.out.println("Nodes::       " + Formatter.formatNodes(Whiteboard.getProminentNodes()));
        System.out.print(">>>");
        String command = input.nextLine().trim();
        if(command.toLowerCase().equals("q")){
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
            return false;
        } else if (command.toLowerCase().equals("help") || command.toLowerCase().equals("?")) {
            Core.println(HELP_STRING);
        } else if (command.toLowerCase().equals("end")) {
            Core.println("Ending this conversation");
            Whiteboard.putAll();
            PA.save();
            Whiteboard.clearAll();
        }else {
            String[] terms = command.split(" ");
            Triplet<String, String, ArrayList<String>> parsedCommands = null;
            //Have the full class and method name


            if (terms.length == 1) {
                if (terms[0].equals("test"))
                    PA.test();
                else
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
                } else if (Core.contains(terms, "has")) {
                    String[] hasSplit = command.split("has");
                    if (StrRep.isStringRepresentation(hasSplit[1])) {

                    } else {

                    }
                }
            } else {
                //Prefix Notation
                switch (terms[0]) {
                    case "create":
                        Whiteboard.addNode(PA.createNode(command.replace("create", "").trim()));
                        break;
                    case "view":
                        Core.println(Formatter.viewNode(Whiteboard.search(command.replace("view", ""))));
                        break;
                    case "add":
                        command = command.replace("add", "pa.Node.add");
                        parsedCommands = parseFull(command);
                        break;
                    default:
                        return true;
                }
            }


            ExecutionFlow returnedObject = null;

            if (parsedCommands != null) {
                returnedObject = invoke(parsedCommands.getValue0(), parsedCommands.getValue1(), parsedCommands.getValue2());
            }

            if (returnedObject != null && returnedObject.completedP()) {
                if (returnedObject.getResult() instanceof Node)
                    Whiteboard.addNode((Node) returnedObject.getResult());
                else if (returnedObject.getResult() instanceof String)
                    System.out.println(returnedObject.getResult());
            }
        }
        Whiteboard.cycle();
        return true;
    }


}
