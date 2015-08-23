package main;

import funct.Core;
import pa.*;
import util.returnTuple;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * Created by @devinmcgloin on 8/17/2015.
 * TODO currently REPL only works for static library methods in which the parameters are explicit. You cannot call class methods on the objects of their class.
 *
 *
 * TODO support sets for NBNs
 *
 * EXAMPLE SYNTAX: add @esb height
 * ARRAYLIST OF LDBN: {#1,#2 , #3} Spaces do not matter
 */
public class REPL {
    ArrayList<Node> NBNnodes = new ArrayList<>();
    ArrayList<Node> LDBNnodes = new ArrayList<>();
    Scanner input = new Scanner(System.in);
    private final String ldataID = "#";
    private final String nounID = "@";
    private final String setIDopen = "{";
    private final String setIDclose = "}";

    static Logger logger = Logger.getLogger(REPL.class);

    public REPL(){}

    private ExecutionFlow invoke(String className, String methodName, ArrayList<String> argumentID){
        try {
            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for(Method method : methods){
                logger.debug(method.getName() + " == " + methodName);
                logger.debug(method.getGenericParameterTypes().length + " == " + argumentID.size());
                if(method.getName().equals(methodName) && method.getGenericParameterTypes().length == argumentID.size() && matchTypes(method, argumentID)){
                    ExecutionFlow flow = new ExecutionFlow(method);
                    for(String id : argumentID){
                        if(id.startsWith(nounID)){
                            Node node = parse(id);
                            if(node != null)
                                flow.applyArgument(node);
                            else
                                return null;
                        }else if(id.startsWith(ldataID)) {
                            Node node = parse(id);
                            if(node != null)
                                flow.applyArgument(node);
                            else
                                return null;
                        }else if(id.startsWith(setIDopen) && id.contains(nounID)) {
                            ArrayList<Node> array = parseArray(id);
                            if(Core.checkArray(array))
                                flow.applyArgument(array);
                            else
                                return null;
                        }else if(id.startsWith(setIDopen) && id.contains(ldataID)) {
                            ArrayList<Node> array = parseArray(id);
                            if(Core.checkArray(array))
                                flow.applyArgument(array);
                            else
                                return null;
                        }else if(id.startsWith(setIDopen)) {
                            flow.applyArgument(parseArrayString(id));
                        }else{
                            flow.applyArgument(id);
                        }
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
            switch (terms[0]) {
                case "add":
                    if (command.contains(nounID)) {
                        return parseCommand(command.replace("add", "Noun.add"));
                    } else if (command.contains(ldataID)) {
                        return parseCommand(command.replace("add", "LDATA.add"));
                    } else return null;
                case "remove":
                    if (command.contains(nounID)) {
                        return parseCommand(command.replace("remove", "Noun.rm"));
                    } else if (command.contains(ldataID)) {
                        return parseCommand(command.replace("remove", "LDATA.rm"));
                    } else return null;
                case "update":
                    if (command.contains(nounID)) {
                        return parseCommand(command.replace("update", "Noun.rm"));
                    } else if (command.contains(ldataID)) {
                        return parseCommand(command.replace("update", "LDATA.rm"));
                    } else return null;
                case "getldata":
                    return parseCommand(command.replace("getldata", "PA.getLDATA"));
                case "getnoun":
                    return parseCommand(command.replace("getnoun", "PA.getNoun"));
                case "inherit":
                    return parseCommand(command.replace("inherit", "SetLogic.xINHERITy"));
                case "put":
                    return parseCommand(command.replace("put", "PA.put"));
                case "createnoun":
                    return parseCommand(command.replace("createnoun", "PA.createNBN"));
                case "createldata":
                    return parseCommand(command.replace("createldata", "PA.createLDBN"));
                case "get":
                    if (command.contains(nounID)) {
                        return parseCommand(command.replace("get", "Noun.get"));
                    } else if (command.contains(ldataID)) {
                        return parseCommand(command.replace("get", "LDATA.get"));
                    } else return null;
                case "keys":
                    if (command.contains(nounID)) {
                        return parseCommand(command.replace("keys", "Noun.getKeys"));
                    } else if (command.contains(ldataID)) {
                        return parseCommand(command.replace("keys", "LDATA.getKeys"));
                    } else return null;
                default:
                    return null;
            }

        }
    }


    private Node parse(String N1){
        if(N1.startsWith("@")) {
            if (N1.length() == 2 && N1.replace(nounID, "").matches("\\d+")) {
                return NBNnodes.get(Integer.parseInt(N1.replace(nounID, "")) - 1);
            } else {
                for (Node node : NBNnodes) {
                    for (String name : node.getName()) {
                        if (name.equals(N1)) {
                            return node;
                        }
                    }
                }
                return null;
            }
        }else if(N1.startsWith("#")){
            if(N1.length() == 2 && N1.replace(ldataID, "").matches("\\d+")) {
                return LDBNnodes.get(Integer.parseInt(N1.replace(ldataID, "")) - 1);
            }else{
                for(Node node : LDBNnodes){
                    if(LDATA.getTitle(node).equals(N1)){
                        return node;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private ArrayList<Node> parseArray(String array){
        if(array.contains("@")) {
            array = array.replace(setIDopen, "");
            array = array.replace(setIDclose, "");
            String[] split = array.split(",");
            ArrayList<Node> nbns = new ArrayList<>();
            for (String node : split) {
                nbns.add(parse(node.trim()));
            }
            return nbns;
        }else if (array.contains("#")){
            array = array.replace(setIDopen, "");
            array = array.replace(setIDclose, "");
            String[] split = array.split(",");
            ArrayList<Node> ldbns = new ArrayList<>();
            for (String node : split){
                ldbns.add(parse(node.trim()));
            }
            return ldbns;
        }else return null;
    }

    private ArrayList<String> parseArrayString(String array){
        array = array.replace(setIDopen, "");
        array = array.replace(setIDclose, "");
        String[] split = array.split(",");
        ArrayList<String> text = new ArrayList<>();
        for (String term : split){
            text.add(term.trim());
        }
        return text;
    }

    private <T> String formatNodes(ArrayList<T> items){
        String output = "";
        for (int i = 0; i < items.size(); i++){
            output += (i + 1) + "{ " + items.get(i).toString() + " } ";
        }
        return output;
    }

    public void cycle(){
        System.out.println("\n");
        System.out.println("NBN::       " + formatNodes(NBNnodes));
        System.out.println("LDBN::      " + formatNodes(LDBNnodes));
        System.out.print(">>>");
        String command = input.nextLine().trim();
        if(command.toLowerCase().equals("q")){
            return;
        }
        returnTuple parsedCommands = parseCommand(command);
        if(parsedCommands != null) {
            ExecutionFlow flow = invoke((String) parsedCommands.first, (String) parsedCommands.second, (ArrayList<String>) parsedCommands.third);
            if (flow != null && flow.getResult() != null) {
                Type resultType = flow.getResult().getClass();
                if (resultType.getTypeName().equals("pa.NBN")) {
                    replace((NBN) flow.getResult());
                } else if (resultType.getTypeName().equals("pa.LDBN")) {
                    replace((LDBN) flow.getResult());
                } else {
                    System.out.println(flow.getResult());
                }
            }
        }
        cycle();
    }


    private void replace(Node node){
        if(node.isP("NBN")) {
            for (Node term : NBNnodes) {
                if (term.getTitle().equals(node.getTitle())) {
                    int index = NBNnodes.indexOf(term);
                    NBNnodes.remove(term);
                    NBNnodes.add(index, node);
                    return;
                }
            }
            NBNnodes.add(node);
        }else if(node.isP("ldata")){
            for(Node term : LDBNnodes){
                if(term.getTitle().equals(node.getTitle())){
                    int index = NBNnodes.indexOf(term);
                    LDBNnodes.remove(term);
                    LDBNnodes.add(index, node);
                    return;
                }
            }
            LDBNnodes.add(node);
        }
    }

    private boolean matchTypes(Method m, ArrayList<String> arguments){
        Type [] genType = m.getGenericParameterTypes();
        if(genType.length != arguments.size())
            return false;
        boolean [] appliedP = new boolean[genType.length];
        for(int i = 0; i < genType.length; i++){
            for(String argument : arguments){
                if(argument.startsWith("@")){
                    if(genType[i].getTypeName().equals("pa.NBN"))
                        appliedP[i] = true;
                }else if(argument.startsWith("#")){
                    if(genType[i].getTypeName().equals("pa.LDBN"))
                        appliedP[i] = true;
                }else if(genType[i].getTypeName().equals("java.lang.String"))
                    appliedP[i] = true;
            }
        }

        for (boolean item : appliedP){
            if(!item)
                return false;
        }
        return true;
    }
}
