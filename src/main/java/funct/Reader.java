package funct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 8/4/15
 */
public final class Reader {

    static Logger logger = Logger.getLogger(Reader.class);


    private Reader() {
    }

    /**
     * Currently prints out the contents of the CSV file to std out. TODO eventually we want to map the values of a csv
     * to a set of nodes. With the user specifiying what keys are appropriate for each collumn.
     *
     * @param file
     */
    public static void readCSV(String file) {
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
        } catch (IOException e) {
            System.err.printf("File IO error");
        }
    }

    /**
     * TODO Bulk add from a file. taking the nouns from the definitions and adding them as is or has relationships.
     * @param file
     */
    public static void readDict(String file) {

    }

    /**
     * TODO save commands and allow it to load them back in a nd execute.
     * @param file
     */
    public static void readHistory(String file) {

    }


}
