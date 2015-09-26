package funct;

import org.apache.log4j.Logger;
import org.javatuples.Pair;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/20/15.
 * For small utility methods
 * Small change to push
 */
public class Core {

    static Logger logger = Logger.getLogger(Core.class);


    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    public static boolean all(boolean[] list) {
        for (boolean bool : list) {
            if (!bool)
                return false;
        }
        return true;
    }

    public static <T> boolean checkArray(ArrayList<T> terms) {
        for (Object item : terms) {
            if (item == null)
                return false;
        }
        return true;
    }


    public static <T> T getVal(ArrayList<Pair<String, T>> pairs, String key) {
        for (Pair<String, T> pair : pairs) {
            if (pair.getValue0().equals(key))
                return pair.getValue1();
        }
        return null;
    }

    public static <T> boolean contains(T[] list, T val) {
        for (T item : list) {
            if (item.equals(val)) {
                return true;
            }
        }
        return false;
    }
}
