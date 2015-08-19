package main;

import pa.*;
import util.returnTuple;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by @devinmcgloin on 8/17/2015.
 * TODO currently repl only works for static library methods in which the parameters are explecit. You cannot call class methods on the objects of their class.
 *
 *
 * TODO support sets for NBNs
 *
 * EXAMPLE SYNTAX: add @esb height
 * ARRAYLIST OF LDBN: {#1,#2 , #3} Spaces do not matter
 */
public class REPL {
    ArrayList<NBN> NBNnodes = new ArrayList<>();
    ArrayList<LDBN> LDBNnodes = new ArrayList<>();
    Scanner input = new Scanner(System.in);
    private final String ldataID = "#";
    private final String nounID = "@";
    private final String setIDopen = "{";
    private final String setIDclose = "}";


    public REPL(){}

    private ExecutionFlow invoke(String className, String methodName, ArrayList<String> argumentID){
        try {
            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for(Method method : methods){
                if(method.getName().equals(methodName) && method.getGenericParameterTypes().length == argumentID.size()){
                    ExecutionFlow flow = new ExecutionFlow(method);
                    for(String id : argumentID){
                        if(id.startsWith(nounID)){
                            NBN node = parseNBN(id);
                            if(node != null)
                                flow.applyArgument(node);
                            else
                                return null;
                        }else if(id.startsWith(ldataID)) {
                            LDBN node = parseLDBN(id);
                            if(node != null)
                                flow.applyArgument(node);
                            else
                                return null;
                        }else if(id.startsWith(setIDopen) && id.contains(nounID)) {
                            ArrayList<NBN> array = parseArrayNBN(id);
                            if(checkArray(array))
                                flow.applyArgument(array);
                            else
                                return null;
                        }else if(id.startsWith(setIDopen) && id.contains(ldataID)) {
                            ArrayList<LDBN> array = parseArrayLDBN(id);
                            if(checkArray(array))
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
                        System.out.println("Method executed");
                        return flow;
                    }else{
                        System.out.println("Method not executed");
                    }
                    break;
                }else {
//                    System.out.println("Method not found...");
//                    System.out.println("Desired: " + methodName);
//                    System.out.println("  Found: " + method.getName() + "\n");


                }

            }
        }catch(ClassNotFoundException e){
            System.out.println("Class not found...");
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
            for(String term : everythingElse.split(" ")){
                arguments.add(term);
            }

            returnTuple tuple = new returnTuple(className, methodName, arguments);
            return tuple;
        }else{
            if(terms[0].equals("add")){
                if(command.contains(nounID)){
                    return parseCommand(command.replace("add", "Noun.add"));
                }else if(command.contains(ldataID)){
                    return parseCommand(command.replace("add", "LDATA.add"));
                }else return null;
            }else if(terms[0].equals("remove")){
                if(command.contains(nounID)){
                    return parseCommand(command.replace("remove", "Noun.rm"));
                }else if(command.contains(ldataID)){
                    return parseCommand(command.replace("remove", "LDATA.rm"));
                }else return null;
            }else if(terms[0].equals("update")){
                if(command.contains(nounID)){
                    return parseCommand(command.replace("update", "Noun.rm"));
                }else if(command.contains(ldataID)){
                    return parseCommand(command.replace("update", "LDATA.rm"));
                }else return null;
            }else if(terms[0].equals("getldata")){
                return parseCommand(command.replace("getldata", "PA.getLDATA"));
            }else if(terms[0].equals("getnoun")){
                return parseCommand(command.replace("getnoun", "PA.getNoun"));
            }else if(terms[0].equals("inherit")){
                return parseCommand(command.replace("inherit", "SetLogic.xINHERITy"));
            }else if(terms[0].equals("put")){
                return parseCommand(command.replace("put", "PA.put"));
            } else return null;

        }
    }


    private NBN parseNBN(String N1){
        if(N1.length() == 2) {
            return NBNnodes.get(Integer.parseInt(N1.replace(nounID, "")) - 1);
        }else{
            for(NBN node : NBNnodes){
                for(String name : Noun.getName(node)){
                    if(name.equals(N1)){
                        return node;
                    }
                }
            }
            return null;
        }
    }

    private LDBN parseLDBN(String L1){
        if(L1.length() == 2) {
            return LDBNnodes.get(Integer.parseInt(L1.replace(ldataID, "")) - 1);
        }else{
            for(LDBN node : LDBNnodes){
                if(LDATA.getTitle(node).equals(L1)){
                    return node;
                }
            }
            return null;
        }
    }

    private ArrayList<NBN> parseArrayNBN(String array){
        array = array.replace(setIDopen, "");
        array = array.replace(setIDclose, "");
        String[] split = array.split(",");
        ArrayList<NBN> nbns = new ArrayList<>();
        for (String node : split){
            nbns.add(parseNBN(node.trim()));
        }
        return nbns;
    }

    private ArrayList<LDBN> parseArrayLDBN(String array){
        array = array.replace(setIDopen, "");
        array = array.replace(setIDclose, "");
        String[] split = array.split(",");
        ArrayList<LDBN> ldbns = new ArrayList<>();
        for (String node : split){
            ldbns.add(parseLDBN(node.trim()));
        }
        return ldbns;
    }

    private <T> boolean checkArray(ArrayList<T> terms){
        for(Object item : terms ){
            if(item == null)
                return false;
        }
        return true;
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


    private void clearTerminal(){
        for(int i = 0; i < 14; i++){
            System.out.println();
        }
    }

    public void cycle(){
        System.out.print("\n\n\n\n");
        System.out.println("NBN::  " + formatNodes(NBNnodes));
        System.out.println("LDBN:: " + formatNodes(LDBNnodes));
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


    private void replace(NBN node){
        for(NBN term : NBNnodes){
            if(term.getTitle().equals(node.getTitle())){
                int index = NBNnodes.indexOf(term);
                NBNnodes.remove(term);
                NBNnodes.add(index, node);
                return;
            }
        }
        NBNnodes.add(node);
    }

    private void replace(LDBN node){
        for(LDBN term : LDBNnodes){
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
