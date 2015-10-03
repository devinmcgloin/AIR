package funct;

import logic.SetLogic;
import pa.Node;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author devinmcgloin
 * @version 9/23/15
 */
public class Formatter {
    public static <T> String formatList(ArrayList<T> items) {
        StringBuilder output = new StringBuilder();
        output.append("[");
        for (int i = 0; i < items.size(); i++) {
            if (i == items.size() - 1)
                output.append(items.get(i).toString());
            else
                output.append(items.get(i).toString()).append(", ");
        }
        output.append("]");
        return output.toString();
    }

    @SafeVarargs
    public static <T> String formatList(T... items) {
        StringBuilder output = new StringBuilder();
        output.append("[");
        for (int i = 0; i < items.length; i++) {
            if (i == items.length - 1)
                output.append(items[i].toString());
            else
                output.append(items[i].toString()).append(", ");
        }
        output.append("]");
        return output.toString();
    }


    /**
     * @param n
     *
     * @return
     */
    public static String viewNode(Node n) {
        StringBuilder stringBuilder = new StringBuilder();
        if (n != null) {
            int depth = 1;
            stringBuilder.append(stringSpacer(depth)).append(Node.getTitle(n)).append("\n");
            for (String kid : Node.getKeys(n)) {
                if (kid.startsWith("^")) {
                    stringBuilder.append(stringSpacer(depth * 4)).append("├── ").append(kid).append("\n");
                    for (String kidKid : Node.getCarrot(n, kid)) {
                        stringBuilder.append(stringSpacer(depth * 8)).append("├── ").append(kidKid).append("\n");

                    }
                } else {
                    stringBuilder.append(stringSpacer(depth * 4)).append("├── ").append(kid).append("\n");
                    String kidKid = Node.get(n, kid);
                    if (kidKid != null)
                        stringBuilder.append(stringSpacer(depth * 8)).append("├── ").append(kidKid).append("\n");
                }
            }
        } else {
            return "";
        }
        return stringBuilder.toString();
    }

    public static String quickView(Node n) {
//        ArrayList<String> logicalParents = Node.getCarrot(n, "^logicalParents");
//        return !logicalParents.isEmpty() ? String.format("%s <-- %s", logicalParents.get(0), Node.getTitle(n)) : String.format("%s <-- %s", "N/A", Node.getTitle(n));
        Optional<Node> lp = SetLogic.getLogicalParent(n);
        return lp.isPresent()
                ? String.format("%s <-- %s", Node.getTitle(n), Node.getTitle(lp.get()))
                : String.format("%s <-- %s", Node.getTitle(n), "N/A");
    }

    private static String stringSpacer(int i) {
        String returnString = "";
        for (int j = 0; j < i; j++) {
            returnString += " ";
        }
        return returnString;
    }
}
