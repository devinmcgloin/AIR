package logic;

import funct.Core;
import funct.Stats;
import pa.Node;
import pa.PA;
import util.keyVal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * comment to push
 * you push your comment. what are you trying to say ambiguous comment?
 *
 * okay now i know
 * Created by Blazej on 8/15/2015.
 */
public final class Comparison {

//    public static void getDiff(Node a, Node b){
//        //Might be able to pass in "fidelity" level for OF?
//
//    }
//
//    public static void compareBy(metric?, Node a, Node b){
//        //idk what metric means...
//        //int ordering for things
//    }
//
//    public static void stableSort(metric? , ArrayList<Node> nodes){
//        //idk what metric means...
//        //sort by a second qualifier if two are equal, without disturbing the order of the first
//    }
//
//    public static void compare(Node a, Node b){
//        //proximity. should be very simple.
//        //it should be simple because it could be used by the AI to try and decide if a new node that's being talked about
//            //might belong to another set. (Would allow it to ask if x is y and then inherit more keys it could ask about.
//    }



    /**
     * Returns probability of seeing a number of the value (val) or GREATER within the set of nodes.
     * EX: getProbability( <cars>, topspeed, 220 )
     *         returns: 98.5%       (only 1.5% chance of a car having this top speed or greater)
     * This return agrees to the standard with most cdfs and also allows you to see if you're too low or
     *      too high on the distribution.     *
     * Mathspeak:
     * Calculates the Student's T Cumulative Distribution Function from -INFINTY to the T-Score of the Val within the
     *      t-distribution of the set.
     *
     * @param set
     * @param key
     * @param val
     * @return
     */
    public static double getProbability(ArrayList<Node> set, String key, String val){

        ArrayList<keyVal> dist = getDistribution(set, key);

        double sigma = (Double)Core.getVal(dist, "s");
        int df = (Integer)Core.getVal(dist, "df");

        return Stats.tcdf(sigma, df);


    }

    /**
     * Assumes you've already found correct place in the OF nodes.
     * @param set
     * @param key
     */
    public static ArrayList<keyVal> getDistribution(ArrayList<Node> set, String key){

        ArrayList<keyVal> dist = new ArrayList<keyVal>();

        Node ldbn = PA.searchExactTitle(key);
        if(ldbn.equals(null)){
            System.out.println("Comparison: You shit outta luck");
            return null;
        }

        double mean;
        int count = 0;
        double total = 0;
        double sd = 0;
        double q1, q2, q3; //q2 is median, q1 is middle of first half of data set.

        ArrayList<Double> values = new ArrayList<Double>();
//        ArrayList<String> units = LDATA.getUnits(ldbn);

        String value = "";
        //Check to see if they have the value, getCarrot the total
        for(Node node: set){
            value = Search.simpleSearch(node, key);
            if(value.startsWith("^")){
                System.out.println("Comparison: Yo i didn't even have that key or value, homes");
                continue;
            }
            if(value.contains("^")){
                System.out.println("Comparison: Yo I ain't gonna look into OF nodes                           ...bitch");
                //All these things probably belong to a similar set so should have a similar OF structure anyway.
                //Whatever you send me should have the right values in the right place already.
                continue;
            }

            value = value.split(" ")[0];
            if(LDATA.isNumeric(value)){
                double tmp = Double.parseDouble(value);
                values.add(tmp);
                total += tmp;
            }
            count++;


            //Check if correct unit


            //Kep track of the most frequent unit, use that.

        }
        //Get the mean
        mean = total/count;

        double diffSumSq = 0;

        for(double val : values){
            diffSumSq += (val - mean)*(val-mean);
        }

        //Standard deviation
        sd = diffSumSq/count;

        Collections.sort(values);


        if(values.size()%2 != 0){
            int middle = (count+1)/2;
            q1 = getMedian( values.subList(0, middle ) );
            q2 = values.get( middle );
            q3 = getMedian( values.subList(middle , values.size()) );
        }else{
            int mid = count/2;
            int mid2 = mid+1;
            double m1, m2;
            m1 = values.get(mid);
            m2 = values.get(mid2);


            q1 = getMedian( values.subList( 0, mid ));
            q2 = (m1 +m2)/2;
            q3 = getMedian( values.subList( mid, count));


        }


        keyVal tmp = new keyVal("s", sd);
        dist.add(tmp);
        tmp = new keyVal("q1", q1);
        dist.add(tmp);
        tmp = new keyVal("q2", q2);
        dist.add(tmp);
        tmp = new keyVal("q3", q3);
        dist.add(tmp);
        tmp = new keyVal("n", count);
        dist.add(tmp);
        tmp = new keyVal("mean", mean);
        dist.add(tmp);
        tmp = new keyVal("df", count-1);
        dist.add(tmp);
        tmp = new keyVal("total", total);
        dist.add(tmp);
        tmp = new keyVal("min", values.get(0) );
        dist.add(tmp);
        tmp = new keyVal("max", values.get(values.size()-1) );






        return dist;

    }

    private static double getMedian(List<Double> values){

        Collections.sort(values);

        if(values.size()%2 != 0){
            int middle = (values.size()+1)/2;
            return values.get(middle);
        }else{
            double mid1 = values.get( values.size()/2 );
            double mid2 = values.get( values.size()/2 + 1);
            return (mid1+mid2)/2;
        }



    }

//    /**
//     * @param metric
//     * @param A
//     * @param B
//     * @return
//     */
//    public static int compareBy(String metric, Node A, Node B) {
//
//    }



}
