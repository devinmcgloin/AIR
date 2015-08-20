package main;

import org.apache.log4j.Logger;
import pa.PA;
import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/3/15.
 * Terminal is a command parser for the time being.
 */
public final class Terminal {

    static Logger logger = Logger.getLogger(Terminal.class);


    public String getHelp() {
        return String.format(
                "PATHS must be formatted as follows:\n" +
                        "'nouns/places/nations'\n" +
                        "\nCommands:\n" +
                        "(ADD    | +  ): Add node at current directory\n" +
                        "(DEL    | -  ): PATH\n" +
                        "(PRINT  | ls ): List directory.\n" +
                        "(BACK   | .. ): Back one level.\n" +
                        "(CD     | cd ): Return to root.\n" +
                        "(RENAME | mv ): PATH - newName\n" +
                        "(:PQ         ): Activates the PA Query System\n" +
                        "(Help   | ?  ): For help.\n\n");
    }

    //Calls save for whole project

}
