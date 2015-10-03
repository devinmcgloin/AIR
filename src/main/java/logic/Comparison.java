package logic;

import funct.Core;
import funct.Predicate;
import funct.Stats;
import memory.Notepad;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import pa.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Blazej
 * @version 8/15/2015
 */
public final class Comparison {
    static Logger logger = Logger.getLogger(Comparison.class);

    /**
     * TODO What does this return?
     *
     * @param a
     * @param b
     */
    public static void getDiff(Node a, Node b) {
        //Might be able to pass in "fidelity" level for OF?

    }

    /**
     * @param comparator a means of comaparison between nodes. EG horsepower, or sq ft.
     * @param a
     * @param b
     *
     * @return
     */
    public static int compareBy(Comparator<Node> comparator, Node a, Node b) {
        return comparator.compare(a, b);
    }

    /**
     * @param comparator
     * @param nodes
     */
    public static void stableSort(Comparator<Node> comparator, ArrayList<Node> nodes) {
        //idk what metric means...
        //sort by a second qualifier if two are equal, without disturbing the order of the first
    }

    /**
     * TODO dont know what this returns either.
     *
     * @param a
     * @param b
     */
    public static void compare(Node a, Node b) {
        //proximity. should be very simple.
        //it should be simple because it could be used by the AI to try and decide if a new node that's being talked about
        //might belong to another set. (Would allow it to ask if x is y and then inherit more keys it could ask about.
    }


    /**
     * Returns probability of seeing a number of the value (val) or GREATER within the set of nodes. EX: getProbability(
     * <cars>, topspeed, 220 ) returns: 98.5%       (only 1.5% chance of a car having this top speed or greater) This
     * return agrees to the standard with most cdfs and also allows you to see if you're too low or too high on the
     * distribution.     * Mathspeak: Calculates the Student's T Cumulative Distribution Function from -INFINTY to the
     * T-Score of the Val within the t-distribution of the set.
     *
     * @param set
     * @param key
     * @param val todo why is this passed in?
     *
     * @return
     */
    public static double getProbability(ArrayList<Node> set, String key, String val) {

        ArrayList<Pair<String, Double>> dist = getDistribution(set, key);

        if (Core.getVal(dist, "s") != null && Core.getVal(dist, "df") != null) {
            Double sigma = Core.getVal(dist, "s");

            Double df = Core.getVal(dist, "df");

            if (df != null && sigma != null) {
                return Stats.tcdf(sigma, df);
            }
        }

        //TODO need to deal with invalid return types.
        return -123.23432413134342534;

    }

    /**
     * Assumes you've already found correct place in the OF nodes.
     *
     * @param set
     * @param key
     */
    public static ArrayList<Pair<String, Double>> getDistribution(ArrayList<Node> set, String key) {


        Node ldbn = Notepad.searchByTitle(key);
        if (ldbn == null) {
            logger.error("Comparison: You shit outta luck");
            return null;
        }

        double mean;
        int count = 0;
        double total = 0;
        double sd = 0;
        double q1, q2, q3; //q2 is median, q1 is middle of first half of data set.

        ArrayList<Double> values = new ArrayList<>();
//        ArrayList<String> units = LDATA.getUnits(ldbn);

        String value = "";
        //Check to see if they have the value, getCarrot the total
        for (Node node : set) {
            value = Search.simpleSearch(node, key);
            if (value.startsWith("^")) {
                logger.warn("Comparison: Yo i didn't even have that key or value, homes");
                continue;
            }
            if (value.contains("^")) {
                logger.warn("Comparison: Yo I ain't gonna look into OF nodes                           ...bitch");
                //All these things probably belong to a similar set so should have a similar OF structure anyway.
                //Whatever you send me should have the right values in the right place already.
                continue;
            }

            value = value.split(" ")[0];
            if (Predicate.isNumeric(value)) {
                double tmp = Double.parseDouble(value);
                values.add(tmp);
                total += tmp;
            }
            count++;


            //Check if correct unit


            //Kep track of the most frequent unit, use that.

        }
        //Get the mean
        mean = total / count;

        double diffSumSq = 0;

        for (double val : values) {
            diffSumSq += (val - mean) * (val - mean);
        }

        //Standard deviation
        sd = diffSumSq / count;

        Collections.sort(values);


        if (values.size() % 2 != 0) {
            int middle = (count + 1) / 2;
            q1 = getMedian(values.subList(0, middle));
            q2 = values.get(middle);
            q3 = getMedian(values.subList(middle, values.size()));
        } else {
            int mid = count / 2;
            int mid2 = mid + 1;
            double m1, m2;
            m1 = values.get(mid);
            m2 = values.get(mid2);


            q1 = getMedian(values.subList(0, mid));
            q2 = (m1 + m2) / 2;
            q3 = getMedian(values.subList(mid, count));


        }


        ArrayList<Pair<String, Double>> dist = new ArrayList<>();
        Pair<String, Double> tmp = new Pair<>("s", sd);
        dist.add(tmp);
        tmp = new Pair<>("q1", q1);
        dist.add(tmp);
        tmp = new Pair<>("q2", q2);
        dist.add(tmp);
        tmp = new Pair<>("q3", q3);
        dist.add(tmp);
        tmp = new Pair<>("n", (double) count);
        dist.add(tmp);
        tmp = new Pair<>("mean", mean);
        dist.add(tmp);
        tmp = new Pair<>("df", (double) count - 1);
        dist.add(tmp);
        tmp = new Pair<>("total", total);
        dist.add(tmp);
        tmp = new Pair<>("min", values.get(0));
        dist.add(tmp);
        tmp = new Pair<>("max", values.get(values.size() - 1));
        dist.add(tmp);


        return dist;

    }

    public static double getMedian(List<Double> values) {

//        Collections.sort(values);

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double d : values) {
            stats.addValue(d);
        }
        return stats.getPercentile(50);

//        if (values.size() % 2 != 0) {
//            int middle = (values.size() + 1) / 2;
//            return values.get(middle);
//        } else {
//            double mid1 = values.get(values.size() / 2);
//            double mid2 = values.get(values.size() / 2 + 1);
//            return (mid1 + mid2) / 2;
//        }


    }
}
