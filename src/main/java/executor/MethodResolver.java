package executor;

import funct.Core;
import funct.Formatter;
import funct.Parser;
import funct.StrRep;
import memory.Notepad;
import org.javatuples.Triplet;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class MethodResolver {
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
            parsedCommands = Parser.parseFull(command);
        } else if (Core.contains(terms, "like") || Core.contains(terms, "is") || Core.contains(terms, "called") || Core.contains(terms, "has")) {
            //Infix Notation
            if (Core.contains(terms, "like")) {
                command = command.replace("like", ",");
                command = "logic.SetLogic.xLikey " + command;
                parsedCommands = Parser.parseFull(command);
            } else if (Core.contains(terms, "is")) {
                command = command.replace("is", ",");
                command = "logic.SetLogic.xINHERITy " + command;
                parsedCommands = Parser.parseFull(command);
            } else if (Core.contains(terms, "called")) {
                String[] calledSplit = command.split("called");
                command = "pa.Node.add " + calledSplit[0].trim() + ", \"^name\", \"" + calledSplit[1].trim() + "\"";
                parsedCommands = Parser.parseFull(command);
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
                parsedCommands = Parser.parseFull(command);
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
                    parsedCommands = Parser.parseFull(command);
                    break;
                default:
                    return null;
            }
        }
        return parsedCommands;
    }
}
