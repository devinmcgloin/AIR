package reader;

import funct.Core;
import funct.Formatter;
import funct.StrRep;
import logic.Scribe;
import memory.Notepad;
import org.apache.commons.csv.CSVRecord;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/2/15.
 */
public class CSVReader {


    public CSVReader() {
    }

    public ArrayList<Node> readIn(ArrayList<CSVRecord> csvRecords) {
        ArrayList<Node> parsed = new ArrayList<>();
        ArrayList<Node> parsedHeader = matchHeader(csvRecords.get(0));

        for (CSVRecord record : csvRecords) {
            Node n = PA.createNode(record.get(0));
            for (int i = 1; i < record.size(); i++) {
                for (int j = 0; j < parsedHeader.size(); j++) {
                    if (StrRep.isStringRepresentation(record.get(i))) {

                        Core.println(Formatter.formatList(n.toString(),
                                parsedHeader.get(j).toString(), record.get(i)));
                        Core.println("");

                        parsed.add(Scribe.addHighLevel(n, parsedHeader.get(j),
                                StrRep.getStringRep(record.get(i))));

                    }
                }
            }
        }

        Core.println(Formatter.formatList(parsed));

        for (Node n : parsed) {
            Core.println(Formatter.viewNode(n));
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
            Core.println(header.get(i));
            Node n = Notepad.search(header.get(i));
            if (n != null) {
                Core.println(Formatter.quickView(n));
                keyNodes.add(n);
            }
        }
        return keyNodes;
    }

}
