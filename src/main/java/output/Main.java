package output;


import fileReader.FileReader;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import pa.PA;

/**
 * Alright bub, This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 *
 * @author devinmcgloin
 * @author Blazej
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);
    static Options options = new Options();


    public static void main(String[] args) {

        logger.info("Entering application.");

        options.addOption(Option.builder("db").longOpt("db-path").desc("DB folder to read files from")
                .argName("FILE_PATH").hasArg().required()
                .build());

        options.addOption("r", "repl", false, "Launch air in REPL mode.");

        options.addOption(Option.builder("c").longOpt("csv-fileReader").desc("Read files in from CSV.")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("d").longOpt("dict-fileReader").desc("Read in standard dictionary files")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("h").longOpt("hist-fileReader").desc("Read in history to DB.")
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
            formatter.printHelp("AIR", options);
            System.exit(1);
        }

        PA.setDB(cmd.getOptionValue("db"));

        if (cmd.hasOption("r")) {

            REPL repl = new REPL();

            boolean cont = true;
            while (cont) {
                cont = repl.cycle();
            }
            PA.save();
        } else if (cmd.hasOption("c")) {
            FileReader.readCSV(cmd.getOptionValue("c"));
        } else if (cmd.hasOption("d")) {
            FileReader.readDict(cmd.getOptionValue("d"));
        } else if (cmd.hasOption("h")) {
            FileReader.readHistory(cmd.getOptionValue("h"));
        }

        logger.info("Exiting application.");
    }

}
