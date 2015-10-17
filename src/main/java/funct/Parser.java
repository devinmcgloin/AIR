package funct;

import memory.Notepad;
import org.javatuples.Triplet;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/5/15.
 */
public class Parser {

    public static Triplet<String, String, ArrayList<String>> parseFull(String command) {
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

    public static Triplet<String, String, ArrayList<String>> commandPreprocessor(String command) {
        String[] terms = command.split(" ");
        Triplet<String, String, ArrayList<String>> parsedCommands = null;
        //Have the full class and method name


        if (terms.length == 1) {
            if (terms[0].equals("test"))
                PA.test();
            else
                Notepad.search(terms[0]);
        } else if (terms[0].contains(".") && terms.length > 1) {
            parsedCommands = parseFull(command);
        } else if (Core.contains(terms, "like") || Core.contains(terms, "is") || Core.contains(terms, "called") || Core.contains(terms, "has")) {
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
                command = "method.Scribe.addHighLevel";
                for (String has : hasSplit) {
                    if (StrRep.isStringRepresentation(has)) {
                        command += "~" + has + ",";
                        continue;
                    }
                    command += has + ",";
                }
                parsedCommands = parseFull(command);
            }
        } else {
            //Prefix Notation
            switch (terms[0]) {
                case "create":
                    Notepad.addNode(PA.createNode(command.replace("create", "").trim()));
                    break;
                case "view":
                    Core.println(Formatter.viewNode(Notepad.search(command.replace("view", ""))));
                    break;
                case "add":
                    command = command.replace("add", "pa.Node.add");
                    parsedCommands = parseFull(command);
                    break;
                default:
                    return null;
            }
        }
        return parsedCommands;
    }

}
