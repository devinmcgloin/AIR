package speed;

import com.google.common.base.Stopwatch;
import funct.Core;
import memory.Notepad;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.Random;
import java.util.Stack;

import static funct.Const.NAME;


/**
 * @author devinmcgloin
 * @version 10/16/15.
 */
public class Speedy {

    static Logger logger = Logger.getLogger(Speedy.class);
    private static Random rand = new Random();
    private static Stack<String> content;
    private static Node n;
    private static int nodesAdded = 0;
    private static int termsAdded = 0;
    private static Stack<String> nodeTitleForSearching = new Stack<>();
    private static Stack<String> hashSearching = new Stack<>();
    private static Stack<String> namesSearching = new Stack<>();

    public static void speed() {
        Core.println("Begin generation");
        content = genWords(800000);
        Core.println("Completed generation");

        Stopwatch timer = Stopwatch.createStarted();
        PA.start();
        logger.info(String.format("Loading DB took %s\n", timer.stop()));

        n = PA.createNode(content.pop());
        nodesAdded++;

        timer.reset().start();

        while (content.size() > 2) {
            populate();
        }
        logger.info(String.format("Populating took %s\n", timer.stop()));
        logger.info(String.format("%d nodes added\n", nodesAdded));
        logger.info(String.format("%d terms added\n", termsAdded));

        timer.reset().start();
        PA.save();
        logger.info(String.format("Saving took %s\n", timer.stop()));

        logger.info(String.format("Hash Searching for %s items\n", nodeTitleForSearching.size()));
        timer.reset().start();
        while (!nodeTitleForSearching.isEmpty()) {
            Notepad.searchByTitle(nodeTitleForSearching.pop());
        }
        logger.info(String.format("Searching took %s\n", timer.stop()));

        logger.info(String.format("Hash Searching for %s items\n", hashSearching.size()));
        timer.reset().start();
        while (!hashSearching.isEmpty()) {
            PA.generalSearch(hashSearching.pop());
        }
        logger.info(String.format("Hash Searching took %s\n", timer.stop()));

        logger.info(String.format("Hash Searching for %s items\n", namesSearching.size()));
        timer.reset().start();
        while (!namesSearching.isEmpty()) {
            PA.searchName(namesSearching.pop());
        }
        logger.info(String.format("Name Searching took %s\n", timer.stop()));

    }

    private static void populate() {
        int choice = rand.nextInt(4);
        switch (choice) {
            case 0:
                nodesAdded++;
                PA.put(n);
                nodeTitleForSearching.push(content.peek());
                n = PA.createNode(content.pop());
                break;
            case 1:
                termsAdded++;
                n = Node.add(n, content.pop());
                break;
            case 2:
                termsAdded++;
                hashSearching.push(content.peek());
                n = Node.add(n, content.pop(), content.pop());
                break;
            case 3:
                termsAdded++;
                namesSearching.push(content.peek());
                n = Node.add(n, NAME.toString(), content.pop());
                break;
            default:
                logger.warn(String.format("Wasted term %s : %d", content.peek(), choice));
        }
    }


    public static Stack<String> genWords(int length) {
        StringBuilder word = new StringBuilder();
        Stack<String> words = new Stack<>();
        for (int i = 0; i < length; i++) {
            int wordLength = rand.nextInt(15) + 3;

            for (int j = 0; j < wordLength; j++)
                word.append((char) nextCharPoint());

            words.push(word.toString());
            word = new StringBuilder();
        }

        return words;
    }

    public static int nextCharPoint() {
        return rand.nextInt(26) + 97;
    }
}
