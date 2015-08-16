package main;

import pa.ExecutionFlow;
import pa.LDBN;
import pa.NBN;
import util.returnTuple;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Blazej on 6/17/2015.
 */
public class REPL {
    ArrayList<NBN> NBNnodes = new ArrayList<>();
    ArrayList<LDBN> LDBNnodes = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    public REPL(){}

    private ExecutionFlow invoke(String className, String methodName, ArrayList<String> argumentID){
        try {
            Class execution = Class.forName(className);
            Method[] methods = execution.getMethods();
            for(Method method : methods){
                if(method.getName().equals(methodName) && method.getGenericParameterTypes().length == argumentID.size()){
                    ExecutionFlow flow = new ExecutionFlow(method);
                    for(String id : argumentID){
                        if(id.startsWith("@")){
                            flow.applyArgument(parseNBN(id));
                        }else if(id.startsWith("#")) {
                            flow.applyArgument(parseLDBN(id));
                        }else if(id.startsWith("[") && id.contains("@")) {
                            flow.applyArgument(parseArrayNBN(id));
                        }else if(id.startsWith("[") && id.contains("#")) {
                            flow.applyArgument(parseArrayLDBN(id));
                        }else if(id.startsWith("[") && id.contains(":")) {
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

    private util.returnTuple parseCommand(String command){
        int period = command.indexOf(".");
        int space = command.indexOf(" ");
        String className = "pa." + command.substring(0, period);
        String methodName = command.substring(period + 1, command.indexOf("(")).trim();
        String everythingElse = command.substring(command.indexOf("(") + 1, command.length() - 1);
//        everythingElse = everythingElse.replace("(", "");
//        everythingElse = everythingElse.replace(")", "");
        ArrayList<String> arguments = new ArrayList<>();
        for(String term : everythingElse.split(",")){
            arguments.add(term);
        }

        util.returnTuple tuple = new returnTuple(className, methodName, arguments);
        return tuple;
    }

    private NBN parseNBN(String N1){
        System.out.println(N1);
        System.out.println(Integer.parseInt(N1.replace("@", ""))- 1);
        return NBNnodes.get(Integer.parseInt(N1.replace("@", "")) - 1);
    }

    private LDBN parseLDBN(String L1){
        System.out.println("" + L1.charAt(1));
        return LDBNnodes.get(Integer.getInteger("" + L1.charAt(1)) - 1);
    }

    private ArrayList<NBN> parseArrayNBN(String array){
        array = array.replace("[", "");
        array = array.replace("]", "");
        String[] split = array.split(",");
        ArrayList<NBN> nbns = new ArrayList<>();
        for (String node : split){
            nbns.add(parseNBN(node));
        }
        return nbns;
    }

    private ArrayList<LDBN> parseArrayLDBN(String array){
        array = array.replace("[", "");
        array = array.replace("]", "");
        String[] split = array.split(",");
        ArrayList<LDBN> ldbns = new ArrayList<>();
        for (String node : split){
            ldbns.add(parseLDBN(node));
        }
        return ldbns;
    }

    private ArrayList<String> parseArrayString(String array){
        array = array.replace("[", "");
        array = array.replace("]", "");
        String[] split = array.split(",");
        ArrayList<String> text = new ArrayList<>();
        for (String term : split){
            text.add(term);
        }
        return text;
    }

    private <T> String formatNodes(ArrayList<T> items){
        String output = "";
        for (Object item : items){
            output += item.toString() + ", ";
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
        System.out.println("NBN: " + formatNodes(NBNnodes));
        System.out.println("LDBN: " + formatNodes(LDBNnodes));
        System.out.print(">>>");
        String command = input.nextLine().trim();
        if(command.toLowerCase().equals("q")){
            return;
        }
        returnTuple parsedCommands = parseCommand(command);
        ExecutionFlow flow = invoke((String) parsedCommands.first, (String) parsedCommands.second, (ArrayList<String>) parsedCommands.third);
        if(flow != null && flow.getResult() != null) {
            Type resultType = flow.getResult().getClass();
            System.out.println(resultType.getTypeName());
            System.out.println("NBN");
            if (resultType.getTypeName().equals("pa.NBN")) {
                replace((NBN)flow.getResult());
            } else if (resultType.getTypeName().equals("pa.LDBN")) {
                replace((LDBN) flow.getResult());
            } else {
                System.out.println(flow.getResult());
            }
        }
        cycle();
    }

    public void replace(NBN node){
        for(NBN term : NBNnodes){
            if(term.getTitle().equals(node.getTitle())){
                NBNnodes.remove(term);
                NBNnodes.add(node);
                return;
            }
        }
        NBNnodes.add(node);
    }

    public void replace(LDBN node){
        for(LDBN term : LDBNnodes){
            if(term.getTitle().equals(node.getTitle())){
                LDBNnodes.remove(term);
                LDBNnodes.add(node);
                return;
            }
        }
        LDBNnodes.add(node);
    }
}
