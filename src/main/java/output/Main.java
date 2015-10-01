package output;


import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import pa.PA;

/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 * TODO Bulk add from a file.
 * TODO Pass in flags for different modes
 * TODO save commands and allow it to load them back in a nd execute.
 * TODO Basic matrix manipulation and reading in dictionaries, taking the nouns from the definitions and adding htem as is or has relationships.


 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);
    static Options options = new Options();


    public static void main(String[] args) {

        logger.info("Entering application.");
        options.addOption("r", "repl", false, "Launch air in REPL mode.");
        options.addOption("n", "nulp", false, "Launch air in NULP mode.");

        options.addOption(Option.builder("c").longOpt("csv-reader").desc("Read files in from CSV.")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("d").longOpt("dict-reader").desc("Read in standard dictionary files")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("rh").longOpt("history-reader").desc("Read in history to DB.")
                .argName("FILE_PATH").hasArg()
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            // parse the command line arguments
            cmd = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("air", options);
            System.exit(0);
        }

        if (cmd.hasOption("r")) {

            REPL repl = new REPL();

            boolean cont = true;
            while (cont) {
                cont = repl.cycle();
            }
            PA.save();
        }

        logger.info("Exiting application.");
    }

}
