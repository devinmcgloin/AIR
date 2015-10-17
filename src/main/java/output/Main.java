package output;


import nulp.NULP;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import pa.PA;
import reader.Reader;

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

        options.addOption(Option.builder("graph").longOpt("graph-path").desc("Graph folder to read files from")
                .argName("FILE_PATH").hasArg().required()
                .build());

        options.addOption("r", "repl", false, "Launch air in REPL mode.");
        options.addOption("n", "nulp", false, "Launch air in NULP mode.");

        options.addOption(Option.builder("c").longOpt("csv-reader").desc("Read files in from CSV.")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("d").longOpt("dict-reader").desc("Read in standard dictionary files")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("h").longOpt("hist-reader").desc("Read in history to DB.")
                .argName("FILE_PATH").hasArg()
                .build());

        options.addOption(Option.builder("s")
                .longOpt("speed-test")
                .desc("Tests the DB for efficiency")
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
        } else if (cmd.hasOption("n")) {
            //TODO this is 100% going to change.
            NULP nulp = new NULP();

            boolean cont = true;
            while (cont) {
                cont = nulp.cycle();
            }
            PA.save();
        } else if (cmd.hasOption("c")) {
            Reader.readCSV(cmd.getOptionValue("c"));
        } else if (cmd.hasOption("d")) {
            Reader.readDict(cmd.getOptionValue("d"));
        } else if (cmd.hasOption("h")) {
            Reader.readHistory(cmd.getOptionValue("h"));
        } else if (cmd.hasOption("s")) {
            Speedy.speed();
        } else if (cmd.hasOption("g")) {
            GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(cmd.getOptionValue("g"));
            try (Transaction tx = graphDb.beginTx()) {
                // Database operations go here
                tx.success();
            }
        }


        logger.info("Exiting application.");
    }

}
