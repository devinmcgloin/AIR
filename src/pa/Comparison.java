package pa;

import funct.Core;
import funct.Stats;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import util.keyVal;
import funct.Core.*;

/**
 * comment to push
 * you push your comment. what are you trying to say ambiguous comment?
 *
 * okay now i know
 * Created by Blazej on 8/15/2015.
 */
public final class Comparison {

    static Logger logger = Logger.getLogger(Comparison.class);

//    public static void getDiff(NBN a, NBN b){
//        //Might be able to pass in "fidelity" level for OF?
//
//    }
//
//    public static void compareBy(metric?, NBN a, NBN b){
//        //idk what metric means...
//        //int ordering for things
//    }
//
//    public static void stableSort(metric? , ArrayList<NBN> nodes){
//        //idk what metric means...
//        //sort by a second qualifier if two are equal, without disturbing the order of the first
//    }
//
//    public static void compare(NBN a, NBN b){
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
    public static double getProbability(ArrayList<NBN> set, String key, String val){

        double nullHyp = Double.parseDouble(val);

        ArrayList<keyVal> dist = getDistribution(set, key);

        if(dist == null){

            return 0.0;
        }




        double s = (Double)Core.getVal(dist, "s");
        double mean = (Double)Core.getVal(dist, "mean");
        int n = (Integer)Core.getVal(dist, "n");
        int df = n-1;

        //System.out.println(s);

        if(df == 0) {
            logger.warn("Cannot do proper distribution with only one value.");

            return 0;
        }

        //The purpose of the t is to reject the null hypothesis that the value is the true mean.
        double t = (mean - nullHyp) / (s/Math.sqrt(n));

        //System.out.println(t);

        return Stats.tcdf(t, df);


    }

    /**
     * Assumes you've already found correct place in the OF nodes.
     * @param set
     * @param key
     */
    public static ArrayList<keyVal> getDistribution(ArrayList<NBN> set, String key){

        ArrayList<keyVal> dist = new ArrayList<keyVal>();

        LDBN ldbn = PA.getLDATA(key);
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
        ArrayList<String> units = LDATA.getUnits(ldbn);

        String value = "";
        String unit = "";
        String stdUnit = new String("nada");
        //Check to see if they have the value, get the total
        for(NBN node: set){

            value = Noun.simpleSearch(node, key);
            if(value.startsWith("^")){
                logger.warn("Node: "+node.getTitle()+ " did not contain the value or key for the key: "+key);
                continue;
            }
            if(value.contains("^")){
                logger.warn("Distribution function will not look into OF nodes for a key.");
                //All these things probably belong to a similar set so should have a similar OF structure anyway.
                //Whatever you send me should have the right values in the right place already.
                continue;
            }

            String[] temp = value.split(" ");
            value = temp[0];
            unit = temp[1];

            if(LDATA.isNumeric(value)){
                //Set standard by first value.
                if(stdUnit.equals("nada")){
                    logger.debug("Standard Unit Picked for Distribution: " + unit);
                    stdUnit = new String(unit);
                }
                double tmp = Double.parseDouble(value);
                if(unit.equals(stdUnit)){
                    values.add(tmp);
                }else{
                    logger.error("We should be converting values here. (At writing time, method for doing so not yet decided.)");
                }
                 //FUCK make sure it's first the same unit as the standard unit. If not, convert.
                total += tmp;


            }
            count++;




        }

        if(values.size() == 0)
            return null;

        //Get the mean
        mean = total/count;

        sd = Stats.stdDev(values);


        Collections.sort(values);



        if(values.size() == 1){
            q1 = values.get(0);
            q2 = q1;
            q3 = q1;
        }else if(values.size() == 2){
            q1 = values.get(0);
            q3 = values.get(1);
            q2 = (q1+q3)/2;
        }else if(values.size()%2 != 0){
            int middle = (count+1)/2;
            q1 = getMedian( values.subList(0, middle ) );
            q2 = getMedian(values);
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

        if(values.size() == 1)
            return values.get(0);
        if(values.size()==0) {
            logger.error("Cannot get median of no values");
            return 0.0;
        }

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



}
