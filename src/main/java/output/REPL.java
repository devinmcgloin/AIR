package output;

import executor.ExecutionFlow;
import executor.Invoker;
import funct.Core;
import funct.Formatter;
import funct.Parser;
import memory.Notepad;
import memory.Whiteboard;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * <p/>
 * currently REPL only works for static library methods in which the parameters are explicit. You cannot call class
 * methods on the objects of their class.
 * <p/>
 * <p/>
 * TODO arrow up to get last command
 * TODO abstact alot of the logic of running things to an executor package, and focus here on running the repl. So parsing, and so on.
 *
 * @author devinmcgloin
 * @version 8/17/2015.
 */
@SuppressWarnings ("FieldCanBeLocal")
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

    public REPL() {
    }



    public boolean cycle() {
        System.out.println("\n");
        System.out.println("Nodes:    " + Formatter.formatList(Whiteboard.getProminentNodes()));
        System.out.print(">>> ");
        String command = input.nextLine().trim();
        if (command.toLowerCase().equals("q")) {
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
        } else {
            Triplet<String, String, ArrayList<String>> parsedCommands = Parser.commandPreprocessor(command);


            ExecutionFlow returnedObject = null;

            if (parsedCommands != null) {
                returnedObject = Invoker.invoke(parsedCommands.getValue0(), parsedCommands.getValue1(), parsedCommands.getValue2());
            }

            if (returnedObject != null && returnedObject.completedP()) {
                if (returnedObject.getResult() instanceof Node)
                    Notepad.addNode((Node) returnedObject.getResult());
                else if (returnedObject.getResult() instanceof String)
                    System.out.println(returnedObject.getResult());
                else if (returnedObject.getResult().getClass().equals(ArrayList.class))
                    Core.println(Formatter.formatList((ArrayList<Object>) returnedObject.getResult()));

            }
        }
        Whiteboard.addAllNotepadNodes();
        Whiteboard.cycle();
        return true;
    }


}
