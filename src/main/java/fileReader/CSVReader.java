package fileReader;

import parse.Parser;
import funct.Formatter;
import memory.Notepad;
import method.Scribe;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/2/15.
 */
public class CSVReader {
    static Logger logger = Logger.getLogger(CSVReader.class);



    public CSVReader() {
    }

    public ArrayList<Node> readIn(ArrayList<CSVRecord> csvRecords) {
        ArrayList<Node> parsed = new ArrayList<>();
        ArrayList<Node> parsedHeader = matchHeader(csvRecords.get(0));

        for (int a = 1; a < csvRecords.size(); a++) {
            Node n = PA.createNode(csvRecords.get(a).get(0));
            CSVRecord record = csvRecords.get(a);
            for (int i = 1; i < record.size(); i++) {

                    if (Parser.isStringRepresentation.apply(record.get(i))) {

                        logger.debug(Formatter.formatList(n.toString(),
                                parsedHeader.get(i - 1).toString(), record.get(i)));

                        n = Scribe.addHighLevel(n, parsedHeader.get(i - 1),
                                Parser.getStringRep(record.get(i)));

                    }

            }
            parsed.add(n);
        }

        logger.debug(Formatter.formatList(parsed));

        for (Node n : parsed) {
            logger.debug(Formatter.viewNode(n));
        }

        return parsed;
    }


    /**
     * Parses the header file but leaves the first index as that will generally be used as the title node.
     *
     * @param header
     *
     * @return
     */
    public ArrayList<Node> matchHeader(CSVRecord header) {
        ArrayList<Node> keyNodes = new ArrayList<>();
        for (int i = 1; i < header.size(); i++) {
            logger.debug(header.get(i));
            Node n = Notepad.search(header.get(i));
            if (n != null) {
                logger.debug(Formatter.quickView(n));
                keyNodes.add(n);
            }
        }
        return keyNodes;
    }

}
