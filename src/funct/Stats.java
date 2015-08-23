package funct;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/20/15.
 * Written by balzepoop109 on the/same/date.
 * comment to push
 */
public class Stats {

    public static double stdDev(ArrayList<Double> values){

        double[] tmp = new double[values.size()];
        for(int i =0; i<values.size(); i++){
            tmp[i] = values.get(i);
            //System.out.println(values.get(i));
        }

        StandardDeviation obj = new StandardDeviation();
        return obj.evaluate(tmp);
    }

    /**
     * Given a t score (xbar - null)/(s/sqrt(n)) and a df (n-1) we return the integral of the
     * t-distribution from -infinity to the t score.
     * @param t
     * @param df
     * @return
     */
    public static double tcdf(double t, int df){

        TDistribution tmp = new TDistribution(df);
        //System.out.println( "Student T's Cumulative Probability Distribution Function: " + t.cumulativeProbability(1.0589) );

        return tmp.cumulativeProbability(t);



    }

    public static double inverseCumulativeProbability(double p, int df){
        TDistribution t = new TDistribution(df);
        return t.inverseCumulativeProbability(p);
    }

    public static double getMean(ArrayList<Double> values){
        double sum = 0.0;
        for(double v : values){
            sum+=v;
        }
        return sum/values.size();
    }

    public static double getProbability(ArrayList<Double> values, double nullHypothesis){
        double ans = 0.0;
        double mean = getMean(values);
        int n = values.size();
        int df = n-1;
        double s = stdDev(values);

        //The purpose of the t is to reject the null hypothesis that the value is the true mean.
        double t = (mean - nullHypothesis) / (s/Math.sqrt(n));

        //System.out.println(t);

        ans = Stats.tcdf(t, df);


        return ans;
    }

    /**
     * Formal usage should be for best guess.
     * Confidence Interval should be a double between .01 and .9999
     * (Don't be a dick, pick something normal like .95 or .97)
     *
     * EX: You have a very ad hoc set. Republican candidates in the last 10 years for primaries. Then you want
     * to guess who is most likely to be chosen. You can run confidence intervals on their age, height, weight,
     * etc. to give ranges that are expected. Try picking a candidate between this height and age and weight, etc.
     * It's the reverse of the getProbability function. That would be best used for asking how good of a chance
     * ONE specific candidate has.
     * @param values
     * @param ci
     * @return
     */
    public static void getConfidenceInterval(ArrayList<Double> values, double ci){

        //FUCK pick a return type for what a "interval" will be in the LDATA/Strawberry Test dream ad hoc nodes.

        double mean;
        double s;
        int n;
        double negativeC; //t-stat ... 1 - ci/2
        double positiveC; //t-stat




    }


}
