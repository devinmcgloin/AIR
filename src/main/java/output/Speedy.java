package output;

import com.google.common.base.Stopwatch;
import funct.Core;
import pa.Node;
import pa.PA;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import static funct.Const.NAME;


/**
 * @author devinmcgloin
 * @version 10/16/15.
 */
public class Speedy {

    private static Random rand = new Random();
    private static Stack<String> content;
    private static Node n;
    private static int nodesAdded = 0;
    private static int termsAdded = 0;
    private static Stack<String> nodeTitleForSearching = new Stack<>();
    private static Stack<String> hashSearching = new Stack<>();
    private static Stack<String> namesSearching = new Stack<>();


    public static void speed() {
        Scanner input = new Scanner(System.in);
        input.next();
        Core.println("Begin generation");
        content = genWords(500000);
        Core.println("Completed generation");

        Stopwatch timer = Stopwatch.createStarted();
        PA.start();
        System.out.printf("Loading DB took %s\n", timer.stop());

        n = PA.createNode(content.pop());
        nodesAdded++;

        timer.reset().start();

        while (content.size() > 2) {
            populate();
        }
        System.out.printf("Populating took %s\n", timer.stop());
        System.out.printf("%d nodes added\n", nodesAdded);
        System.out.printf("%d terms added\n", termsAdded);

        timer.reset().start();
        PA.save();
        System.out.printf("Saving took %s\n", timer.stop());

        timer.reset().start();
        while (!nodeTitleForSearching.isEmpty()) {
            PA.searchExactTitle(nodeTitleForSearching.pop());
        }
        System.out.printf("Searching took %s\n", timer.stop());

        timer.reset().start();
        while (!hashSearching.isEmpty()) {
            PA.generalSearch(hashSearching.pop());
        }
        System.out.printf("Hash Searching took %s\n", timer.stop());

        timer.reset().start();
        while (!namesSearching.isEmpty()) {
            PA.searchName(namesSearching.pop());
        }
        System.out.printf("Name Searching took %s\n", timer.stop());

    }

    private static void populate() {
        int choice = rand.nextInt(5);
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
                hashSearching.push(content.pop());
                n = Node.add(n, content.pop(), content.pop());
                break;
            case 3:
                termsAdded++;
                namesSearching.push(content.pop());
                n = Node.add(n, NAME.toString(), content.pop());
                break;
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
