package main;


import pa.PA;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Entering application.");

        REPL repl = new REPL();
        repl.cycle();
        PA.save();

        logger.info("Exiting application.");
    }

}
