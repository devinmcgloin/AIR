package funct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author devinmcgloin
 * @version 8/4/15
 */
public final class Reader {

    static Logger logger = Logger.getLogger(Reader.class);


    private Reader() {
    }

    /**
     * Currently prints out the contents of the CSV file to std out.
     * TODO eventually we want to map the values of a csv to a set of nodes. With the user specifiying what keys are appropriate for each collumn.
     *
     * @param file
     */
    public static void readCSV(String file) {
        if (file.isEmpty()) {
            System.err.print("Please specify an input file\n");
            System.exit(1);
        }
        try {
            java.io.Reader reader = new FileReader(file);
            ArrayList<CSVRecord> csvRecords = new ArrayList<>();
            for (CSVRecord record : CSVFormat.DEFAULT.parse(reader)) {
                csvRecords.add(record);
            }
            for (CSVRecord rec : csvRecords) {
                System.out.println(rec);
            }

        } catch (FileNotFoundException e) {
            System.err.printf("File %s not found. Please check your path", file);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("File IO error");
            System.exit(1);
        }

        //TODO actual computations. Probably going to be outsourced to a CSVREADER.
    }

    /**
     * TODO Bulk add from a file. taking the nouns from the definitions and adding them as is or has relationships.
     * @param file
     */
    public static void readDict(String file) {
        Optional<Scanner> result = readIn(file);
        if (result.isPresent()) {
            Scanner scanner = result.get();
            while (scanner.hasNextLine()) {
                //TODO add whatever specific dictionary stuff you want here. Prob outsourced to a dict reader.
                Core.println(scanner.nextLine());
            }
        }

    }

    /**
     * TODO save commands and allow it to load them back in a nd execute.
     * @param file
     */
    public static void readHistory(String file) {
        Optional<Scanner> result = readIn(file);
        if (result.isPresent()) {
            Scanner scanner = result.get();

            while (scanner.hasNextLine()) {
                //TODO add whatever specific history stuff you want here. Prob outsourced to a history reader.
                Core.println(scanner.nextLine());
            }
        }
    }


    private static Optional<Scanner> readIn(String file) {
        if (file.isEmpty()) {
            System.err.print("Please specify an input file\n");
            System.exit(1);
        }
        File input = new File(file);
        if (!input.exists() || !input.canRead()) {
            System.err.printf("Input file: %s does not exist, or cannot be read", input);
            System.exit(2);
        }
        try {
            return Optional.of(new Scanner(input));
        } catch (FileNotFoundException e) {
            System.err.printf("Input file: %s does not exist, or cannot be read", input);
            System.exit(2);
        }
        return Optional.empty();
    }
}
