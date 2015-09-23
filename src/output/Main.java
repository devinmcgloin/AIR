package output;


import org.apache.log4j.Logger;
import pa.PA;

/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Entering application.");


        REPL repl = new REPL();

        boolean cont = true;
        while (cont) {
            cont = repl.cycle();
            PA.test();
        }
        PA.save();

        logger.info("Exiting application.");
    }

}
