package output;

import funct.Core;
import funct.Formatter;
import memory.Whiteboard;
import molecule.Molecule;
import nulp.Reader;
import org.apache.log4j.Logger;
import timeline.Timeline;

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
        Core.printf("Nodes : %s\n", Formatter.formatList(Whiteboard.getProminentNodes()));
        Core.print(">>> ");
        String msg = input.nextLine();
        Molecule parsed = Reader.parse(msg);
        Timeline.addEvent(parsed);
        return true;
    }


}
